package com.global.ecommerce.service.authService;

import com.global.ecommerce.Entity.User;
import com.global.ecommerce.repository.UserRepo;
import com.global.ecommerce.security.AppUserDetail;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2; // Import the annotation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Log4j2 // Add the annotation for Log4j2
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        log.info("Fetching all users from the database");
        List<User> users = userRepo.findAll();
        log.debug("Number of users fetched: {}", users.size());
        return users;
    }

    public User findById(Long id) {
        log.info("Finding user with ID: {}", id);
        return userRepo.findById(id).orElseThrow(() -> {
            log.error("User with ID {} not found", id);
            return new RuntimeException("User not found");
        });
    }
    public User findByEmail(String userEmail) {
        log.info("Finding user with Email: {}",userEmail );
        return userRepo.findByEmail(userEmail).orElseThrow(() -> {
            log.error("User with email {} not found", userEmail);
            return new RuntimeException("User not found");
        });
    }

    public boolean userExistsById(Long userId) {
        log.info("Checking if user exists with ID: {}", userId);
        try {
            userRepo.findById(userId).orElseThrow(() -> {
                log.warn("User not found with ID: {}", userId);
                return new UsernameNotFoundException("User not found with ID: " + userId);
            });
            log.debug("User exists with ID: {}", userId);
            return true;
        } catch (UsernameNotFoundException e) {
            log.error("Error checking user existence: {}", e.getMessage());
            return false;
        }
    }

    public User save(User entity) {
        log.info("Saving user: {}", entity.getEmail());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        User savedUser = userRepo.save(entity);
        log.debug("User saved successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);
        Optional<User> user = userRepo.findByEmail(email);
        if (!user.isPresent()) {
            log.error("User not found with email: {}", email);
            throw new UsernameNotFoundException("This User Not Found With the selected Email");
        }
        log.debug("User loaded successfully: {}", user.get().getEmail());
        return new AppUserDetail(user.get());
    }

    private static List<GrantedAuthority> getAuthorities(User user) {
        log.info("Fetching roles for user: {}", user.getEmail());
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                log.debug("Role added: {}", role.getName());
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        }
        return authorities;
    }

    public ResponseEntity<List<User>> findUsersByRolesName(String roleName) {
        log.info("Finding users by role name: {}", roleName);
        List<User> users = userRepo.findByRolesName(roleName);
        log.debug("Number of users found with role {}: {}", roleName, users.size());
        return ResponseEntity.ok(users);
    }
}









//package com.global.ecommerce.service.authService;
//
//
//import com.global.ecommerce.Entity.User;
//import com.global.ecommerce.repository.UserRepo;
//import com.global.ecommerce.security.AppUserDetail;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//@NoArgsConstructor
//public class UserService implements UserDetailsService {
//    @Autowired
//    private UserRepo userRepo;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    public List<User> findAll(){
//        return userRepo.findAll();
//    }
//    public User findById(Long id){
////        return userRepo.findById(id).orElse(null);
//        return userRepo.findById(id).orElseThrow(()->new RuntimeException("user not found"));
//    }
//    public boolean userExistsById(Long userId) {
//        try {
//            userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
//            return true;
//        } catch (UsernameNotFoundException e) {
//            return false;
//        }
//    }
//    public User save(User entity){
//        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
//        return userRepo.save(entity);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<User> user = userRepo.findByEmail(email);
//        if (!user.isPresent()) {
//            throw new UsernameNotFoundException("This User Not Found With select Email");
//        }
////        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), getAuthorities(user.get()));
//        return new AppUserDetail(user.get());
//    }
//
//    private static List<GrantedAuthority> getAuthorities(User user) {
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        if (!user.getRoles().isEmpty()) {
//            user.getRoles().forEach(role -> {
//                authorities.add(new SimpleGrantedAuthority(role.getName()));
//            });
//        }
//        return authorities;
//    }
//    public ResponseEntity<List<User>> findUsersByRolesName(String Rolename){
//        return ResponseEntity.ok( userRepo.findByRolesName(Rolename));
//    }
//}













