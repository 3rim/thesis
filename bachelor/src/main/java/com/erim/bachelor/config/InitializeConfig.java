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
import java.util.*;
import java.util.random.RandomGenerator;

@Configuration
@RequiredArgsConstructor
public class InitializeConfig {

    private final BorrowerRepository repository;
    private static Long ID_COUNTER =0L;
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;


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

            ArrayList<MediaSeries> mediaSeries = new ArrayList<>(
                    Arrays.asList(

                        ));
            //MediaSeries IPAD 7
            MediaSeries ipad7 = MediaSeries
                    .builder()
                    .id(1L)
                    .title("IPad Gen.7")
                    .mediaTyp("IPad")
                    .mediumList(new ArrayList<>())
                    .build();
            //MediaSeries IPAD 9
            MediaSeries ipad9 = MediaSeries
                    .builder()
                    .id(2L)
                    .title("IPad Gen.9")
                    .mediaTyp("IPad")
                    .mediumList(new ArrayList<>())
                    .build();

            MediaSeries matheI = MediaSeries
                    .builder()
                    .id(3L)
                    .title("Mathe I")
                    .mediaTyp("Buch")
                    .ISBN_EAN("554656654")
                    .originalPrice(25.5)
                    .mediumList(new ArrayList<>())
                    .subjects(new HashSet<>(){{
                        add("Mathe");
                    }})
                    .vintage(new HashSet<>(){{
                        add(5);add(6);
                    }})
                    .build();

            mediaSeriesRepository.save(ipad7);
            mediaSeriesRepository.save(ipad9);
            mediaSeriesRepository.save(matheI);

            List<Medium> media = generateRandomMedia(false);
            media.forEach(medium -> {
                medium.setMediaSeries(matheI);
                matheI.getMediumList().add(medium);
            });
            mediaSeriesRepository.save(matheI);

            media = generateRandomMedia(true);
            media.forEach(medium -> {
                medium.setMediaSeries(ipad7);
                ipad7.getMediumList().add(medium);
            });
            mediaSeriesRepository.save(ipad7);

            media = generateRandomMedia(true);
            media.forEach(medium -> {
                medium.setMediaSeries(ipad9);
                ipad9.getMediumList().add(medium);
            });
            mediaSeriesRepository.save(ipad9);


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

    private List<Medium> generateRandomMedia(boolean serialNumber){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        List<Medium> media = new ArrayList<>();
        if(serialNumber){
            for (int i = 0; i<10;i++){
                String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                Medium m = new Medium(ID_COUNTER++, generatedString,null,Status.AVAILABLE,null,null,null);
                media.add(m);
            }
        }else {
            for (int i = 0; i<10;i++){
                Medium m = new Medium(ID_COUNTER++, "",null,Status.AVAILABLE,null,null,null);
                media.add(m);
            }
        }
        return media;
    }
}
