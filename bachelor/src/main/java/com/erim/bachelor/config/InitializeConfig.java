package com.erim.bachelor.config;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import com.erim.bachelor.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class InitializeConfig {

    @Bean
    CommandLineRunner commandLineRunner(InventoryRepository inventoryRepository, UserRepository userRepository){
        return args -> {
            Medium m1 = new Medium(1l,"d");
            inventoryRepository.save(m1);

            ArrayList<Borrower> users = new ArrayList<>(
                    Arrays.asList(
                            new Borrower("TestUser","lastName"),
                            new Borrower("Max maximus","Mustermann"),
                            new Borrower("Max ","Maxi")
                    ));

            userRepository.saveAll(users);

        };
    }
}
