package com.flotta.service.registry;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.MissingRequiredPropertiesException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.enums.UserStatus;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.registry.Role;
import com.flotta.model.registry.User;
import com.flotta.model.registry.UserDetailsImpl;
import com.flotta.repository.registry.RoleRepository;
import com.flotta.repository.registry.UserRepository;
import com.flotta.service.EmailService;
import com.flotta.utility.BooleanWithMessages;
import com.flotta.utility.Validator;

@Service
public class UserService implements UserDetailsService {

  @Value("${spring.profiles.active}")
  private String profile;
  
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailService emailService;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmail(username);
    if (!user.isPresent()) {
      throw new UsernameNotFoundException(username);
    }
    return new UserDetailsImpl(user.get());
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findById(long userId) {
    return userRepository.findById(userId);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  BooleanWithMessages create(User user) {
    BooleanWithMessages eb = new BooleanWithMessages(true);
    Optional<User> userOpt = userRepository.findByEmail(user.getEmail());

    if (userOpt.isPresent()) {
      eb.setFalse();
      eb.addMessage(MessageKey.EMAIL_ALREADY_USED, MessageType.WARNING);
    } else {
      String password = generateKey(16);
      user.setStatus(UserStatus.WAITING_FOR_ACTIVATION);
      user.setPassword(passwordEncoder.encode(password));
      user.setActivationKey(generateKey(16));
      if (emailService.sendMessage(user.getEmail(), "Aktiváció és kezdeti jelszó", emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] { user.getFullName(), user.getActivationKey(), password }))) {
        userRepository.save(user);
      } else {
        eb.addMessage(MessageKey.EMAIL_FAILURE, MessageType.WARNING);
      }
    }
    return eb;
  }

  BooleanWithMessages update(User user, Map<String, Boolean> roles) {
    BooleanWithMessages eb = new BooleanWithMessages(true);
    Set<Role> savableRoles = convertToRoleSet(roles);
    Role adminRole = roleRepository.findByRoleIgnoreCase("admin").get();
    if(lastAdmin(user.getId()) && !(savableRoles.contains(adminRole) && user.getStatus().equals(UserStatus.ENABLED))) {
      eb.setFalse();
      eb.addMessage(MessageKey.NO_REDUCE_ADMIN, MessageType.WARNING);
    } else {
      userRepository.findById(user.getId()).ifPresent(savedUser -> {
        savedUser.setRoles(savableRoles);
        savedUser.setStatus(user.getStatus());
        userRepository.save(savedUser);
      });
      
    }
    return eb;
  }

  BooleanWithMessages activation(String key) {
    BooleanWithMessages eb = new BooleanWithMessages(true);
    Optional<User> userOpt = userRepository.findByActivationKey(key);
    if(userOpt.isPresent()) {
      User user = userOpt.get();
      user.setActivationKey("");
      user.setStatus(UserStatus.ENABLED);
      userRepository.save(user);
      eb.addMessage(MessageKey.SUCCESSFUL_ACTIVATION, MessageType.SUCCESS);
    } else {
      eb.setFalse();
      eb.addMessage(MessageKey.UNKNOWN_ACTIVATION_KEY, MessageType.WARNING);
    }
    return eb;
  }

  BooleanWithMessages changePassword(String email, String oldPsw, String newPsw, String confirmPsw) {
    BooleanWithMessages eb = new BooleanWithMessages(true);
    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (!passwordEncoder.matches(oldPsw, user.getPassword())) {
        eb.addMessage(MessageKey.CURRENT_PASSWORD_INCORRECT, MessageType.WARNING);
        eb.setFalse();
      }
      if (oldPsw.equalsIgnoreCase(newPsw)) {
        eb.addMessage(MessageKey.PASSWORD_NEW_OLD_SAME, MessageType.WARNING);
        eb.setFalse();
      }
      if(!Validator.validPassword(newPsw) && newPsw.contentEquals(confirmPsw)) {
        eb.addMessage(MessageKey.NEW_PASSWORD_INVALID, MessageType.WARNING);
        eb.setFalse();
      }
      if(eb.booleanValue()) {
        user.setPassword(passwordEncoder.encode(newPsw));
        user.setStatus(UserStatus.ENABLED);
        userRepository.save(user);
        eb.setTrue();
        eb.addMessage(MessageKey.PASSWORD_CHANGE_SUCCESSFUL, MessageType.SUCCESS);
      }
    }
    return eb;
  }

  BooleanWithMessages requestNewPassword(String email) {
    BooleanWithMessages em = new BooleanWithMessages(false);
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if(UserStatus.ENABLED.equals(user.getStatus())) {
        String password = generateKey(16);
        user.setPassword(passwordEncoder.encode(password));
        if (emailService.sendMessage(user.getEmail(), "Új jelszó kérése", emailService.createMessageText(EmailService.NEW_PASSWORD, new String[] { user.getFullName(), password }))) {
          userRepository.save(user);
          em.setTrue();
        } else {
          em.addMessage(MessageKey.EMAIL_FAILURE, MessageType.WARNING);
        }
      } else {
        em.addMessage(MessageKey.NOT_ENABLED, MessageType.WARNING);
        return em;
      }
    } else {
      em.addMessage(MessageKey.UNKNOWN_EMAIL, MessageType.WARNING);
    }
    return em;
  }

  boolean updateChargeRatioOfUser(long userId, Optional<ChargeRatioByCategory> chargeRatioOpt) {
    if(chargeRatioOpt.isPresent()) {
      Optional<User> userOpt = userRepository.findById(userId);
      userOpt.ifPresent(user -> {
        user.setChargeRatio(chargeRatioOpt.get());
        userRepository.save(user);
      });
      return userOpt.isPresent();
    }
    return false;
  }

  BooleanWithMessages createFirstAdmin(User user) {
    BooleanWithMessages eb = new BooleanWithMessages(false);
    String password = generateKey(16);
    user.addRole(roleRepository.findByRoleIgnoreCase("admin").get());
    user.setStatus(UserStatus.WAITING_FOR_ACTIVATION);
    user.setPassword(passwordEncoder.encode(password));
    user.setActivationKey(generateKey(16));
    if (emailService.sendMessage(user.getEmail(), "Aktiváció és kezdeti jelszó", emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] { user.getFullName(), user.getActivationKey(), password }))) {
      userRepository.deleteAll();
      userRepository.save(user);
      eb.setTrue();
      eb.addMessage(MessageKey.SUCCESSFUL_REGISTRATION, MessageType.SUCCESS);
    } else {
      eb.addMessage(MessageKey.EMAIL_FAILURE, MessageType.WARNING);
    }
    return eb;
  }

  boolean hasAdmin() {
    Optional<Role> adminOpt = roleRepository.findByRoleIgnoreCase("admin");
    if(adminOpt.isPresent()) {
      Role admin = adminOpt.get();
      return !admin.getUsers().isEmpty();
    } else {
      throw new MissingRequiredPropertiesException();
    }
  }

  boolean hasEnabledAdmin() {
    Optional<Role> adminOpt = roleRepository.findByRoleIgnoreCase("admin");
    if(adminOpt.isPresent()) {
      Role admin = adminOpt.get();
      for(User user : admin.getUsers()) {
        if(UserStatus.ENABLED.equals(user.getStatus())) {
          return true;
        }
      }
      return false;
    } else {
      throw new MissingRequiredPropertiesException();
    }
  }

  private String generateKey(int length) {
    Random random = new Random();
    char[] key = new char[length];
    for (int i = 0; i < key.length; i++) {
      key[i] = (char) ('a' + random.nextInt(26));
    }
    return new String(key);
  }

  private Set<Role> convertToRoleSet(Map<String, Boolean> roles) {
    Set<Role> result = new HashSet<>();
    for (String key : new LinkedList<>(roles.keySet())) {
      Optional<Role> roleOpt = roleRepository.findByRoleIgnoreCase(key);
      roleOpt.ifPresent(role -> result.add(role));
    }
    return result;
  }

    BooleanWithMessages delete(long id) {
      BooleanWithMessages eb = new BooleanWithMessages(true);
      Optional<User> userOpt = findById(id);
      userOpt.ifPresent(user -> {
        if(lastAdmin(id)) {
          eb.setFalse();
          eb.addMessage(MessageKey.NO_REDUCE_ADMIN, MessageType.WARNING);
        } else if(user.deletable()) {
          userRepository.delete(user);
        }
      });
      return eb;
    }
    
    private boolean lastAdmin(long id) {
      Optional<User> userOpt = userRepository.findById(id);
      if(userOpt.isPresent()) {
        User user = userOpt.get();
        if(!user.hasRole("admin")) {
          return false;
        }
        Role adminRole = roleRepository.findByRoleIgnoreCase("admin").get();
        return adminRole.getUsers().size() == 1;
      }
      return false;
    }
    
 // TOTO delete create first admin
    @PostConstruct
    private void createFirstAdmin() {
      if("test".equals(profile)) {
        userRepository.save(createUser("admin@gmail.com", "Admin", "admin", UserStatus.ENABLED));
        userRepository.save(createUser("testuser@gmail.com", "Test User", "testuser", UserStatus.WAITING_FOR_ACTIVATION));
      }
    }
    
    private User createUser(String email, String name, CharSequence psw, UserStatus status) {
      User user = new User();
      user.setEmail(email);
      user.setFullName(name);
      user.setPassword(passwordEncoder.encode(psw));
      user.setStatus(status);
      return user;
    }
}
