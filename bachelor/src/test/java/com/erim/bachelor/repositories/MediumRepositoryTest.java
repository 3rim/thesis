package com.erim.bachelor.repositories;

import com.erim.bachelor.enums.Status;
import com.erim.bachelor.entities.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/*
    Spring, by default,creates an H2 DB for the test. No need to specify it in a properties file.
    Just add h2 in your pom.xml and set its scope to test. Spring Boot should handle the rest.
 */
@SpringBootTest
class MediumRepositoryTest {

    @Autowired
    private MediumRepository mediumRepository;

    @BeforeEach
    void setUp() {
        mediumRepository.deleteAll();

        ArrayList<Medium> media = new ArrayList<>(
                Arrays.asList(
                        Medium.builder().title("Java ist auch eine Insel").status(Status.AVAILABLE).build(),
                        Medium.builder().title("Java ist auch eine Insel").status(Status.AVAILABLE).build(),
                        Medium.builder().title("Java ist auch eine Insel").status(Status.AVAILABLE).build(),
                        Medium.builder().title("Java ist auch eine Insel").status(Status.AVAILABLE).build()
                ));
        mediumRepository.saveAll(media);
    }

    @Test
    void InitMediaExists(){
        List<Medium> actual = mediumRepository.findAll();
        assertThat(actual.size()).isEqualTo(4);
    }


}