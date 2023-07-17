package com.erim.bachelor.config;

import com.erim.bachelor.data.Role;
import com.erim.bachelor.data.Status;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import com.erim.bachelor.repositories.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class InitializeConfig {

    private final BorrowerRepository repository;
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> repository.findBorrowerByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return  authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    CommandLineRunner commandLineRunner(InventoryRepository inventoryRepository, BorrowerRepository borrowerRepository){
        return args -> {
            ArrayList<Medium> media = new ArrayList<>(
                    Arrays.asList(
                            Medium.builder().mediumID(1L).title("Java ist auch eine Insel").status(Status.AVAILABLE).ISBN("51651651").build(),
                            Medium.builder().mediumID(2L).title("Java ist auch eine Insel").status(Status.AVAILABLE).ISBN("51651651").build()
                            /*Medium.builder().title("Java ist auch eine Insel").status(Status.AVAILABLE).ISBN("51651651").build(),
                            Medium.builder().title("Java ist auch eine Insel").status(Status.AVAILABLE).ISBN("51651651").build(),*/

                            /*Medium.builder().title("IPad 8.Gen").status(Status.AVAILABLE).serialNr("F9FFABCDQ1GC").build(),
                            Medium.builder().title("IPad 8.Gen").status(Status.AVAILABLE).serialNr("F8FFABCDQ1GC").build(),
                            Medium.builder().title("IPad 8.Gen").status(Status.AVAILABLE).serialNr("F7FFABCDQ1GC").build(),
                            Medium.builder().title("IPad 8.Gen").status(Status.AVAILABLE).serialNr("F6FFABCDQ1GC").build(),

                            Medium.builder().title("Mathe II").status(Status.AVAILABLE).build(),
                            Medium.builder().title("Mathe II").status(Status.AVAILABLE).build(),
                            Medium.builder().title("Mathe II").status(Status.AVAILABLE).build(),
                            Medium.builder().title("Mathe II").status(Status.AVAILABLE).build()*/

                    ));

            inventoryRepository.saveAll(media);
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(Role.ADMIN);
            ArrayList<Borrower> users = new ArrayList<>(
                    Arrays.asList(
                            Borrower.builder().borrowerID(1L).borrowerNr(1L).email("user@user")
                                    .password("$2a$12$QdF4EsO1zP3wbJVCFCTn6ec.6QrTuJQnZf555ojikHW8910/KcFne")
                                    .roles(roleSet)
                                    .build()
                    ));

            borrowerRepository.saveAll(users);

        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
