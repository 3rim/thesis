package com.erim.bachelor.service;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.enums.Status;
import com.erim.bachelor.exceptions.MediaSeriesNotEmptyException;
import com.erim.bachelor.exceptions.MediumIdExistsException;
import com.erim.bachelor.exceptions.MediumIsBorrowedException;
import com.erim.bachelor.repositories.MediaSeriesRepository;
import com.erim.bachelor.repositories.MediumRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private MediaSeriesRepository mediaSeriesRepository;
    @Mock
    private MediumRepository mediumRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private MediaSeries mediaSeriesIPad;
    private MediaSeries emptyMediaSeries;

    private Medium medium1;
    private Medium borrowedMedium;

    private List<MediaSeries> mediaSeriesList;


    @BeforeEach
    void setUp() {
        Borrower borrower = Borrower.builder()
                .borrowerNr(2L)
                .firstName("Erim")
                .lastName("Medi")
                .build();
        mediaSeriesIPad = MediaSeries.builder()
                .title("IPad 7")
                .mediaTyp("IPad")
                .mediumList(new ArrayList<>())
                .build();

        emptyMediaSeries = MediaSeries.builder()
                .title("Mathe II")
                .mediaTyp("Book")
                .mediumList(new ArrayList<>())
                .build();

        medium1 = Medium.builder()
                .mediumID(1L)
                .build();

        borrowedMedium = Medium.builder()
                .mediumID(2L)
                .status(Status.RENT)
                .borrower(borrower)
                .build();

        mediaSeriesIPad.getMediumList().add(medium1);
        mediaSeriesIPad.getMediumList().add(borrowedMedium);

        mediaSeriesList = new ArrayList<>();
        mediaSeriesList.add(emptyMediaSeries);
        mediaSeriesList.add(mediaSeriesIPad);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getInventoryOverview() {
        when(mediaSeriesRepository.findAll()).thenReturn(mediaSeriesList);
        List<MediaSeries> result = inventoryService.getInventoryOverview();
        assertEquals(mediaSeriesList.size(),result.size());
        verify(mediaSeriesRepository,times(1)).findAll();

    }

    @Test
    void addNewMedium() throws MediumIdExistsException {
        when(mediaSeriesRepository.findById(any())).thenReturn(Optional.ofNullable(mediaSeriesIPad));
        inventoryService.addNewMedium(medium1,mediaSeriesIPad.getId());
        verify(mediaSeriesRepository,times(1)).save(any());
    }

    @Test
    void getMedium() {
        when(mediumRepository.findById(medium1.getMediumID())).thenReturn(Optional.ofNullable(medium1));
        inventoryService.getMedium(1L);
        verify(mediumRepository,times(1)).findById(any());
    }

    @Test
    void deleteMedium() throws MediumIsBorrowedException {
        when(mediumRepository.findById(medium1.getMediumID())).thenReturn(Optional.ofNullable(medium1));
        inventoryService.deleteMedium(medium1.getMediumID());
        verify(mediumRepository,times(1)).findById(any());
        verify(mediumRepository,times(1)).deleteById(any());
    }
    @Test
    void dontDeleteMedium_whenMediumBorrowed() {
        when(mediumRepository.findById(borrowedMedium.getMediumID())).thenReturn(Optional.ofNullable(borrowedMedium));
        assertThrows(MediumIsBorrowedException.class, () -> inventoryService.deleteMedium(borrowedMedium.getMediumID()));
        verify(mediumRepository,times(1)).findById(borrowedMedium.getMediumID());
    }

    @Test
    void createNewMediaSeries() {
        when(mediaSeriesRepository.save(any())).thenReturn(mediaSeriesIPad);
        inventoryService.createNewMediaSeries(mediaSeriesIPad);
        verify(mediaSeriesRepository,times(1)).save(any());
    }

    @Test
    void getMediaSeries_byID() {
        when(mediaSeriesRepository.findById(mediaSeriesIPad.getId())).thenReturn(Optional.ofNullable(mediaSeriesIPad));
        MediaSeries response = inventoryService.getMediaSeries(mediaSeriesIPad.getId());
        verify(mediaSeriesRepository,times(1)).findById(mediaSeriesIPad.getId());

        assertEquals(mediaSeriesIPad,response);
    }

    @Test
    void getMediaSeriesMedia() {
        when(mediaSeriesRepository.findById(mediaSeriesIPad.getId())).thenReturn(Optional.ofNullable(mediaSeriesIPad));

        List<Medium> response = inventoryService.getMediaSeriesMedia(mediaSeriesIPad.getId());

        verify(mediaSeriesRepository,times(1)).findById(mediaSeriesIPad.getId());
        assertEquals(mediaSeriesIPad.getMediumList(),response);
    }

    @Test
    void deleteMediaSeries() throws MediaSeriesNotEmptyException {
        when(mediaSeriesRepository.findById(any())).thenReturn(Optional.ofNullable(emptyMediaSeries));
        inventoryService.deleteMediaSeries(emptyMediaSeries.getId());
        verify(mediaSeriesRepository,times(1)).findById(any());
        verify(mediaSeriesRepository,times(1)).deleteById(any());
    }

    @Test
    void dontDeleteMediaSeries_whenNotEmpty(){
        when(mediaSeriesRepository.findById(any())).thenReturn(Optional.ofNullable(mediaSeriesIPad));

        assertThrows(MediaSeriesNotEmptyException.class, () -> inventoryService.deleteMediaSeries(mediaSeriesIPad.getId()));

        verify(mediaSeriesRepository,times(1)).findById(any());
        verify(mediaSeriesRepository,times(0)).deleteById(any());
    }
}