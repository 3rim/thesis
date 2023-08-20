package com.erim.bachelor.service;

import com.erim.bachelor.enums.Status;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.MediaSeriesRepository;
import com.erim.bachelor.repositories.MediumRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InventoryServiceTest {

    @Mock
    private MediumRepository mediumRepository;

    @Mock
    MediaSeriesRepository mediaSeriesRepository;
    private InventoryService inventoryService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable =  MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryService(mediumRepository, mediaSeriesRepository);

    }
    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void getAllMedia() {
        //when
        inventoryService.getAllMedia();
        //then
        verify(mediumRepository).findAll();
    }

    @Test
    void addNewMedium() {
        Medium medium = Medium.
                builder().
                mediumID(5L).
                status(Status.AVAILABLE).
                serialNr("FFFF1234ABCD").
                build();
        //when
        //inventoryService.addNewMedium(medium);
        //then
        ArgumentCaptor<Medium> mediumArgumentCaptor = ArgumentCaptor.forClass(Medium.class);

        verify(mediumRepository).save(mediumArgumentCaptor.capture());
        Medium capturedMedium = mediumArgumentCaptor.getValue();
        assertThat(capturedMedium).isEqualTo(medium);


    }

    @Test
    void getMediumById() {
    }

    @Test
    void updateMedium() {
    }

    @Test
    void deleteMedium() {
    }
}