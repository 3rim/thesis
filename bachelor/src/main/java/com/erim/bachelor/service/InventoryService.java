package com.erim.bachelor.service;

import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.exceptions.MediaSeriesNotEmptyException;
import com.erim.bachelor.exceptions.MediumIsBorrowedException;
import com.erim.bachelor.repositories.MediaSeriesRepository;
import com.erim.bachelor.repositories.MediumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InventoryService implements IInventoryService {

    private final MediumRepository mediumRepository;
    private final MediaSeriesRepository mediaSeriesRepository;


    @Autowired
    public InventoryService(MediumRepository mediumRepository, MediaSeriesRepository mediaSeriesRepository) {
        this.mediumRepository = mediumRepository;
        this.mediaSeriesRepository = mediaSeriesRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MediaSeries> getInventoryOverview() {
        return mediaSeriesRepository.findAll();
    }


    /**
     * Add new Medium to inventory if id does not exist already otherwise return null.
     *{@inheritDoc}
     * @param medium The new Medium to be added
     * @return null or the added Medium
     */
    @Override
    public Medium addNewMedium(Medium medium,Long mediaSeriesId){
        MediaSeries mediaSeries = mediaSeriesRepository.findById(mediaSeriesId).orElseThrow(NoSuchElementException::new);

        if(!mediumRepository.existsById(medium.getMediumID())){
            medium.setMediaSeries(mediaSeries);
            mediaSeries.getMediumList().add(medium);
            mediaSeriesRepository.save(mediaSeries);
            return medium;
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Medium getMedium(Long id) {
        return mediumRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMedium(Long id) throws MediumIsBorrowedException {
        Medium medium = mediumRepository.findById(id).orElseThrow(NoSuchElementException::new);

        if(medium.isBorrowed())
            throw new MediumIsBorrowedException("Medium:"+medium.getMediumID()+" is borrowed to "+medium.getBorrower().getFullName() );
        else
            mediumRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediaSeries createNewMediaSeries(MediaSeries series) {
        return mediaSeriesRepository.save(series);
    }

    /**
     * {@inheritDoc}
     * @param id ID of the MediaSeries
     */
    @Override
    public MediaSeries getMediaSeries(Long id) {
        return mediaSeriesRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * {@inheritDoc}
     * @param mediaSeriesId ID of MediaSeries
     */
    @Override
    public List<Medium> getMediaSeriesMedia(Long mediaSeriesId) {
        MediaSeries mediaSeries = mediaSeriesRepository.findById(mediaSeriesId).orElseThrow(NoSuchElementException::new);
        return mediaSeries.getMediumList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediaSeries patchMediaSeries(Long id,MediaSeries mediaSeries) {
        if(id==null)
            throw new NoSuchElementException();

        if(!mediaSeriesRepository.existsById(id))
            throw new NoSuchElementException();

        mediaSeries.setId(id);
        return mediaSeriesRepository.save(mediaSeries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMediaSeries(Long mediaSeriesId) throws MediaSeriesNotEmptyException {
        MediaSeries mediaSeries = mediaSeriesRepository.findById(mediaSeriesId).orElseThrow(NoSuchElementException::new);
        if(!mediaSeries.getMediumList().isEmpty())
            throw new MediaSeriesNotEmptyException("MediaSeries:"+mediaSeriesId+"cannot be deleted as long as it contains Media");

        mediaSeriesRepository.deleteById(mediaSeriesId);
    }
}
