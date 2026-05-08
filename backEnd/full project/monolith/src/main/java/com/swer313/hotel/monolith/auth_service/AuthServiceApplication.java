package com.swer313.hotel.monolith.auth_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(ERole.ROLE_CUSTOMER));
                roleRepository.save(new Role(ERole.ROLE_MANAGER));
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
            }
        };
    }

    @Bean
    public CommandLineRunner initDemoUsers(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder encoder
    ) {
        return args -> {
            Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Role managerRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            // Matches the demo credentials shown in the frontend Login page.
            if (!userRepository.existsByEmail("guest@luxstay.com")) {
                User guest = new User("guest", "guest@luxstay.com", encoder.encode("guest123"));
                guest.setRoles(Set.of(customerRole));
                userRepository.save(guest);
            }

            if (!userRepository.existsByEmail("admin@luxstay.com")) {
                User admin = new User("admin", "admin@luxstay.com", encoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole, managerRole));
                userRepository.save(admin);
            }
        };
    }
}