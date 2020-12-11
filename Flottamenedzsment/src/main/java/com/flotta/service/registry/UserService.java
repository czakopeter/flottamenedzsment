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

  private UserRepository userRepository;

  private EmailService emailService;

  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Autowired
  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }

  @Autowired
  public void setRoleRepository(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

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
      if (emailService.sendMessage(user.getEmail(), "Activation email", emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] { user.getFullName(), user.getActivationKey(), password }))) {
        userRepository.save(user);
//        eb.addMessage(MessageKey.USER_SUCCESSFULLY_CREATED, MessageType.SUCCESS);
      } else {
        eb.addMessage(MessageKey.EMAIL_FAILURE, MessageType.WARNING);
      }
    }
    return eb;
  }

  BooleanWithMessages updateUser(long id, Map<String, Boolean> roles) {
    BooleanWithMessages eb = new BooleanWithMessages(true);
    Optional<User> userOpt = userRepository.findById(id);
    Optional<Role> adminRoleOpt = roleRepository.findByRoleIgnoreCase("admin");
    if(userOpt.isPresent() && adminRoleOpt.isPresent()) {
      User user = userOpt.get();
      Set<Role> savableRoles = convertToRoleSet(roles);
      if(user.hasRole("admin") && adminRoleOpt.get().getUsers().size() == 1 && !savableRoles.contains(new Role("admin"))) {
        eb.setFalse();
        eb.addMessage(MessageKey.NO_REDUCE_ADMIN, MessageType.WARNING);
      } else {
        user.setRoles(savableRoles);
        userRepository.save(user);
      }
    }
    return eb;
  }

  BooleanWithMessages activation(String key) {
    BooleanWithMessages eb = new BooleanWithMessages(true);
    Optional<User> userOpt = userRepository.findByActivationKey(key);
    if(userOpt.isPresent()) {
      User user = userOpt.get();
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
    BooleanWithMessages eb = new BooleanWithMessages(false);
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      String password = generateKey(16);
      user.setPassword(passwordEncoder.encode(password));
      user.setStatus(UserStatus.ENABLED);
      if (emailService.sendMessage(user.getEmail(), "Activation email", emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] { user.getFullName(), user.getActivationKey(), password }))) {
        userRepository.save(user);
        eb.setTrue();
      } else {
        eb.addMessage(MessageKey.EMAIL_FAILURE, MessageType.WARNING);
      }
    } else {
      eb.addMessage(MessageKey.UNKNOWN_EMAIL, MessageType.WARNING);
    }
    return eb;
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
    userRepository.deleteAll();
    BooleanWithMessages eb = new BooleanWithMessages(false);
    String password = generateKey(16);
    user.addRole(roleRepository.findByRoleIgnoreCase("admin").get());
    user.setStatus(UserStatus.WAITING_FOR_ACTIVATION);
    user.setPassword(passwordEncoder.encode(password));
    user.setActivationKey(generateKey(16));
    if (emailService.sendMessage(user.getEmail(), "Activation email", emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] { user.getFullName(), user.getActivationKey(), password }))) {
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

  // TOTO delete create first admin
    @PostConstruct
    private void createFirstAdmin() {
      userRepository.save(createUser("admin@gmail.com", "Admin", "admin", UserStatus.ENABLED));
      userRepository.save(createUser("testuser@gmail.com", "Test User", "testuser", UserStatus.ENABLED));
//      userRepository.save(createUser("disableduser@gmail.com", "", "disabled", UserStatus.DISABLED));
    }
    
    private User createUser(String email, String name, CharSequence psw, UserStatus status) {
      User user = new User();
      user.setEmail(email);
      user.setFullName(name);
      user.setPassword(passwordEncoder.encode(psw));
      user.setStatus(status);
      return user;
    }

    public void delete(String email) {
      Optional<User> userOpt = findByEmail(email);
      userOpt.ifPresent(user -> {
        if(user.getStatus().equals(UserStatus.WAITING_FOR_ACTIVATION)) {
          userRepository.delete(user);
        }
      });
    }
}
