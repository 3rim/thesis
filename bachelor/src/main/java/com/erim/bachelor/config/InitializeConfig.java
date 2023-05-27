package com.erim.bachelor.config;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import com.erim.bachelor.repositories.UserRepository;
import org.modelmapper.ModelMapper;
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
            ArrayList<Medium> media = new ArrayList<>(
                    Arrays.asList(
                            new Medium(1L,"IPad"),
                            new Medium(2L,"Java ist auch eine Insel"),
                            new Medium(3L,"Mathe II")
                    ));

            inventoryRepository.saveAll(media);

            ArrayList<Borrower> users = new ArrayList<>(
                    Arrays.asList(
                            new Borrower("TestUser","lastName"),
                            new Borrower("Max maximus","Mustermann"),
                            new Borrower("Max ","Maxi")
                    ));

            userRepository.saveAll(users);

        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
