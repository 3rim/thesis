package com.erim.bachelor.config;

import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfig {

    @Bean
    CommandLineRunner commandLineRunner(InventoryRepository repository){
        return args -> {
            Medium m1 = new Medium(1l,"d");
            repository.save(m1);
        };
    }
}
