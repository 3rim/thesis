package com.erim.bachelor.repositories;

import com.erim.bachelor.data.Status;
import com.erim.bachelor.entities.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/*
    Spring, by default,creates an H2 DB for the test. No need to specify it in a properties file.
    Just add h2 in your pom.xml and set its scope to test. Spring Boot should handle the rest.
 */
@SpringBootTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        inventoryRepository.deleteAll();
        ArrayList<Medium> media = new ArrayList<>(
                Arrays.asList(
                        new Medium(1L,"IPad",Status.AVAILABLE,null,
                                "FF991234ABCD",new HashSet<>(),new HashSet<>(),
                                400.0,"IPad",null,null),
                        new Medium(2L,"Java ist auch eine Insel"),
                        new Medium(3L,"Mathe II")
                ));
        inventoryRepository.saveAll(media);
    }

    @Test
    void InitMediaExists(){
        List<Medium> actual = inventoryRepository.findAll();
        assertThat(actual.size()).isEqualTo(3);
    }


}