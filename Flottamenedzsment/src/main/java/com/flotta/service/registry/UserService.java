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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flotta.enums.UserStatus;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.registry.Role;
import com.flotta.model.registry.User;
import com.flotta.model.registry.UserDetailsImpl;
import com.flotta.repository.registry.RoleRepository;
import com.flotta.repository.registry.UserRepository;
import com.flotta.service.EmailService;
import com.flotta.utility.Validator;

@Service
public class UserService extends ServiceWithMsg implements UserDetailsService {

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

  boolean create(User user) {
    Optional<User> userOpt = userRepository.findByEmail(user.getEmail());

    if (userOpt.isPresent()) {
      appendMsg("Already exists!");
    } else {
      String password = generateKey(16);
      user.setEnabled(false);
      user.setStatus(UserStatus.WAITING_FOR_ACTIVATION);
      user.setPassword(passwordEncoder.encode(password));
      user.setActivationKey(generateKey(16));
      if (emailService.sendMessage(user.getEmail(), "Activation email", emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] { user.getFullName(), user.getActivationKey(), password }))) {
        userRepository.save(user);
        return true;
      } else {
        appendMsg("Email send failed!");
      }
    }
    return false;
  }

  boolean updateUser(long id, Map<String, Boolean> roles) {
    Optional<User> userOpt = userRepository.findById(id);
    userOpt.ifPresent(user -> {
      user.setRoles(convertToRoleSet(roles));
      userRepository.save(user);
    });
    return userOpt.isPresent();
  }

  boolean activation(String key) {
    Optional<User> userOpt = userRepository.findByActivationKey(key);
    userOpt.ifPresent(user -> {
      user.setEnabled(true);
      user.setStatus(UserStatus.ENABLED);
      userRepository.save(user);
    });
    return userOpt.isPresent();
  }

  boolean changePassword(String email, String oldPsw, String newPsw, String confirmPsw) {
    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (passwordEncoder.matches(oldPsw, user.getPassword()) && Validator.validPassword(newPsw) && newPsw.contentEquals(confirmPsw)) {
        user.setPassword(passwordEncoder.encode(newPsw));
        user.setStatus(UserStatus.ENABLED);
        userRepository.save(user);
        return true;
      }
    }
    appendMsg("Problem with the added data!");
    return false;
  }
  
  boolean requestNewPassword(String email) {
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      String password = generateKey(16);
      user.setPassword(passwordEncoder.encode(password));
      user.setActivationKey(generateKey(16));
      user.setStatus(UserStatus.WAITING_FOR_ACTIVATION);
      if (emailService.sendMessage(user.getEmail(), "Activation email", emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] { user.getFullName(), user.getActivationKey(), password }))) {
        userRepository.save(user);
        return true;
      }
    }
    return false;
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

  boolean registrationAvailable() {
    return userRepository.findAllByEnabled(true).isEmpty();
  }

  boolean createFirstAdmin(User user) {
    String password = generateKey(16);
    user.setEnabled(false);
    user.addRole(roleRepository.findByRole("ADMIN").get());
    user.setStatus(UserStatus.WAITING_FOR_ACTIVATION);
    user.setPassword(passwordEncoder.encode(password));
    user.setActivationKey(generateKey(16));
    if (emailService.sendMessage(user.getEmail(), "Activation email", emailService.createMessageText(EmailService.ACTIVATION_AND_INITIAL_PASSWORD, new String[] { user.getFullName(), user.getActivationKey(), password }))) {
      userRepository.save(user);
    } else {
      appendMsg("Email send failed!");
      return false;
    }
    return true;
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
      Optional<Role> roleOpt = roleRepository.findByRole(key.toUpperCase());
      roleOpt.ifPresent(role -> result.add(role));
    }
    return result;
  }

  // TOTO delete create first admin
  @PostConstruct
  private void createFirstAdmin() {
    User user = new User();
    user.setEmail("admin@gmail.com");
    user.setFullName("Admin");
    user.setPassword(passwordEncoder.encode("admin"));
    user.setEnabled(true);
    user.setStatus(UserStatus.ENABLED);
    userRepository.save(user);
  }
}
