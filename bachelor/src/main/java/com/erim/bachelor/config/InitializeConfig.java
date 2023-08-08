package com.erim.bachelor.config;

import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.enums.BorrowerState;
import com.erim.bachelor.enums.Role;
import com.erim.bachelor.enums.Status;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.MediumRepository;
import com.erim.bachelor.repositories.BorrowerRepository;
import com.erim.bachelor.repositories.MediaSeriesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
    CommandLineRunner commandLineRunner(MediumRepository mediumRepository, BorrowerRepository borrowerRepository, MediaSeriesRepository mediaSeriesRepository){
        return args -> {
            ArrayList<Medium> media = new ArrayList<>(
                    Arrays.asList(
                            Medium.builder().mediumID(1L).title("Java ist auch eine Insel").status(Status.AVAILABLE).build(),
                            Medium.builder().mediumID(2L).title("Java ist auch eine Insel").status(Status.AVAILABLE).build()
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
            mediumRepository.saveAll(media);



            ArrayList<MediaSeries> mediaSeries = new ArrayList<>(
                    Arrays.asList(
                            MediaSeries
                                    .builder()
                                    .titel("IPad")
                                    .mediaTyp("IPad")
                                    .available(0)
                                    .build()

                    ));
            MediaSeries series = MediaSeries
                    .builder()
                    .titel("IPad")
                    .mediaTyp("IPad")
                    .build();

            mediaSeriesRepository.save(series);
            Medium m = Medium.builder()
                    .mediumID(10L)
                    .mediaSeries(series)
                    .status(Status.AVAILABLE)
                    .build();
            mediumRepository.save(m);

            ArrayList<Borrower> users = new ArrayList<>(
                    Arrays.asList(
                            //User
                            Borrower.builder().borrowerID(100L).borrowerNr(100L).email("user")
                                    .password("$2a$12$QdF4EsO1zP3wbJVCFCTn6ec.6QrTuJQnZf555ojikHW8910/KcFne")
                                    .roles(new HashSet<Role>(){{
                                        add(Role.USER);
                                    }})
                                    .firstName("user")
                                    .lastName("user")
                                    .dob(LocalDate.now())
                                    .borrowerState(BorrowerState.ACTIVE)
                                    .build(),
                            //Admin
                            Borrower.builder().borrowerID(101L).borrowerNr(101L).email("admin")
                                    .password("$2a$12$QdF4EsO1zP3wbJVCFCTn6ec.6QrTuJQnZf555ojikHW8910/KcFne")
                                    .roles(new HashSet<Role>(){{
                                        add(Role.ADMIN);
                                    }})
                                    .firstName("admin")
                                    .lastName("admin")
                                    .dob(LocalDate.now())
                                    .borrowerState(BorrowerState.ACTIVE)
                                    .build(),
                            //Librarian
                            Borrower.builder().borrowerID(102L).borrowerNr(102L).email("librarian")
                                    .password("$2a$12$QdF4EsO1zP3wbJVCFCTn6ec.6QrTuJQnZf555ojikHW8910/KcFne")
                                    .roles(new HashSet<Role>(){{
                                        add(Role.LIBRARIAN);
                                    }})
                                    .firstName("Librarian")
                                    .lastName("librarian")
                                    .borrowerState(BorrowerState.ACTIVE)
                                    .dob(LocalDate.now())
                                    .build(),
                            //Inventory_Helper
                            Borrower.builder().borrowerID(103L).borrowerNr(103L).email("inventoryHelper")
                                    .password("$2a$12$QdF4EsO1zP3wbJVCFCTn6ec.6QrTuJQnZf555ojikHW8910/KcFne")
                                    .roles(new HashSet<Role>(){{
                                        add(Role.INVENTORY_HELPER);
                                    }})
                                    .firstName("inventoryHelper")
                                    .lastName("inventoryHelper")
                                    .borrowerState(BorrowerState.ACTIVE)
                                    .dob(LocalDate.now())
                                    .build(),

                            Borrower.builder().borrowerID(104L).borrowerNr(104L).email("loanHelper")
                                    .password("$2a$12$QdF4EsO1zP3wbJVCFCTn6ec.6QrTuJQnZf555ojikHW8910/KcFne")
                                    .roles(new HashSet<Role>(){{
                                        add(Role.LOAN_HELPER);
                                    }})
                                    .firstName("loanHelper")
                                    .borrowerState(BorrowerState.ACTIVE)
                                    .lastName("helper")
                                    .dob(LocalDate.now())
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
