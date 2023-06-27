package com.erim.bachelor.config;

import com.erim.bachelor.data.Status;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import com.erim.bachelor.repositories.BorrowerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class InitializeConfig {

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

            ArrayList<Borrower> users = new ArrayList<>(
                    Arrays.asList(
                            new Borrower("TestUser","lastName"),
                            new Borrower("Max maximus","Mustermann"),
                            new Borrower("Max","Maxi")
                    ));

            borrowerRepository.saveAll(users);

        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
