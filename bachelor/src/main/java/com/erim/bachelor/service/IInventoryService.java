package com.erim.bachelor.service;

import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.exceptions.MediaSeriesNotEmptyException;
import com.erim.bachelor.exceptions.MediumIdExistsException;
import com.erim.bachelor.exceptions.MediumIsBorrowedException;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IInventoryService {

    /**
     * Return all MediaSeries
     * @return List with stored MediaSeries
     */
    List<MediaSeries> getInventoryOverview();

    /**
     * Adds a Medium under a MediaSeries
     * @param medium The Medium to be added
     * @param mediaSeriesId ID of MediaSeries
     * @return added Medium
     */
    Medium addNewMedium(Medium medium,Long mediaSeriesId) throws MediumIdExistsException;

    /**
     * Get a Medium by its ID
     * @param id The MediumID
     * @return The Medium
     */
    Medium getMedium(Long id);

    /**
     * Delete a Medium if it is not borrowed by a Borrower.
     * @param id ID of Medium
     * @throws MediumIsBorrowedException if Medium is borrowed but deletion is tried.
     */
    void deleteMedium(Long id) throws MediumIsBorrowedException;

    /**
     * Returns all Media in a MediaSeries.
     * @param mediaSeriesId ID of MediaSeries
     * @return A List containing each Medium in the MediaSeries
     */
    List<Medium> getMediaSeriesMedia(Long mediaSeriesId);

    /**
     * Create new MediaSeries
     * @param mediaSeries The general information for the new MediaSeries like ISBN/EAN,Price...
     * @return new created MediaSeries
     */
    MediaSeries createNewMediaSeries(MediaSeries mediaSeries);

    /**
     * Get MediaSeries by ID
     * @param mediaSeriesId ID of the MediaSeries
     * @return The MediaSeries
     */
    MediaSeries getMediaSeries(Long mediaSeriesId);

    /**
     * Update general information for a MediaSeries
     * @param mediaSeriesId ID of the MediaSeries
     * @param mediaSeries The general information for the new MediaSeries like ISBN/EAN,Price...
     * @return patched MediaSeries
     */
    MediaSeries patchMediaSeries(Long mediaSeriesId, MediaSeries mediaSeries);

    /**
     * Delete a MediaSeries. Deletion is only possible if MediaSeries does not contain Media!
     * @param mediaSeriesId ID of MediaSeries
     * @throws MediaSeriesNotEmptyException When trying to delete a non-empty MediaSeries
     */
    void deleteMediaSeries(Long mediaSeriesId) throws MediaSeriesNotEmptyException;

    /**
     * Search medium by serialnumber.
     *
     * @param serialNr SerialNumber of Medium.
     * @return List containing every medium that matches with serialNr.
     */
    List<Medium> getMediumBySerialNr(String serialNr);
}
