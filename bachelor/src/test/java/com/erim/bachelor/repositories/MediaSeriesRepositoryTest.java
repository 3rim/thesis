package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.MediaSeries;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@DataJpaTest
public class MediaSeriesRepositoryTest {

    @Autowired
    private MediaSeriesRepository mediaSeriesRepository;


    @Test
    void save_ReturnSavedMediaSeries(){
        //Arrange
        MediaSeries mediaSeries = MediaSeries.builder()
                .title("IPad 7.Gen")
                .mediaTyp("IPad")
                .build();
        //Act
        MediaSeries savedMediaSeries = mediaSeriesRepository.save(mediaSeries);
        //Assert
        Assertions.assertNotNull(savedMediaSeries);
    }

    @Test
    void getAll_ReturnsMoreThanOneMediaSeries(){
        MediaSeries mediaSeries = MediaSeries.builder()
                .title("IPad 7.Gen")
                .mediaTyp("IPad")
                .build();
        MediaSeries mediaSeries2 = MediaSeries.builder()
                .title("IPad 8.Gen")
                .mediaTyp("IPad")
                .build();

        //Act
        mediaSeriesRepository.save(mediaSeries);
        mediaSeriesRepository.save(mediaSeries2);
        List<MediaSeries> mediaSeriesList = mediaSeriesRepository.findAll();
        //Assert
        Assertions.assertEquals(2,mediaSeriesList.size());
    }

    @Test
    void findByTitle(){
        MediaSeries mediaSeries = MediaSeries.builder()
                .title("IPad 7.Gen")
                .mediaTyp("IPad")
                .build();

        MediaSeries mediaSeries2 = MediaSeries.builder()
                .title("IPad 8.Gen")
                .mediaTyp("IPad")
                .build();

        //Act
        mediaSeriesRepository.save(mediaSeries);
        mediaSeriesRepository.save(mediaSeries2);
        List<MediaSeries> mediaSeriesList = mediaSeriesRepository.findAll();
        //Assert
        Assertions.assertEquals(2,mediaSeriesList.size());
    }
}
