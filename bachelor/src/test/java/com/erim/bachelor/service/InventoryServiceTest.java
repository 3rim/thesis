package com.erim.bachelor.service;

import com.erim.bachelor.data.Status;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.mockito.Mockito.verify;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    private InventoryService inventoryService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable =  MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryService(inventoryRepository);

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
        verify(inventoryRepository).findAll();
    }

    @Test
    void addNewMedium() {
        Medium medium =new Medium(
                5L,
                "IPad",
                Status.AVAILABLE,
                null,
                "FFFF1234ABCD",
                new HashSet<Integer>(),
                new HashSet<String>(),
                400.0,
                "IPad",
                null,
                null);
        //when
        inventoryService.addNewMedium(medium);
        //then
        ArgumentCaptor<Medium> mediumArgumentCaptor = ArgumentCaptor.forClass(Medium.class);

        verify(inventoryRepository).save(mediumArgumentCaptor.capture());
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