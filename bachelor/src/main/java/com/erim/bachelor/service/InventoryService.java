package com.erim.bachelor.service;

import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.exceptions.MediaSeriesNotEmptyException;
import com.erim.bachelor.exceptions.MediumStillBorrowedException;
import com.erim.bachelor.repositories.MediaSeriesRepository;
import com.erim.bachelor.repositories.MediumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InventoryService {

    private final MediumRepository mediumRepository;
    private final MediaSeriesRepository mediaSeriesRepository;


    @Autowired
    public InventoryService(MediumRepository mediumRepository, MediaSeriesRepository mediaSeriesRepository) {
        this.mediumRepository = mediumRepository;
        this.mediaSeriesRepository = mediaSeriesRepository;
    }

    /**
     * Returns an empty List or all MediaSeries
     * @return List with all MediaSeries stored in database
     */
    public List<MediaSeries> getInventoryOverview() {
        return mediaSeriesRepository.findAll();
    }

    /**
     * Add new Medium to inventory if id does not exist already otherwise return null
     *
     * @param medium The new Medium to be added
     * @return null or the added Medium
     */
    public Medium addNewMedium(Medium medium,Long seriesID){
        MediaSeries mediaSeries = mediaSeriesRepository.findById(seriesID).orElseThrow(NoSuchElementException::new);

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

    public Medium getMediumById(Long id) {
        return mediumRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public  Optional<Medium> updateMedium(Long id,Medium newMedium) {
        //TODO:refactor
        return Optional.of(mediumRepository.findById(id)
                .map(medium -> {
                    //medium.setTitle(newMedium.getTitle());

                    return mediumRepository.save(medium);
                })
                .orElseGet(() -> mediumRepository.save(newMedium)));
    }

    public void deleteMedium(Long id) throws MediumStillBorrowedException {
        Medium medium = mediumRepository.findById(id).orElseThrow(NoSuchElementException::new);

        if(medium.isBorrowed())
            throw new MediumStillBorrowedException("Medium:"+medium.getMediumID()+" is borrowed to "+medium.getBorrower().getFullName() );
        else
            mediumRepository.deleteById(id);
    }

    public MediaSeries createNewMediaSeries(MediaSeries series) {
        return mediaSeriesRepository.save(series);
    }

    public MediaSeries getMediaSeries(Long id) {
        return mediaSeriesRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Medium> getMediaSeriesMedia(Long seriesID) {
        MediaSeries mediaSeries = mediaSeriesRepository.findById(seriesID).orElseThrow(NoSuchElementException::new);
        return mediaSeries.getMediumList();
    }

    public MediaSeries patchMediaSeries(Long id,MediaSeries mediaSeries) {
        if(id==null)
            throw new NoSuchElementException();

        if(!mediaSeriesRepository.existsById(id))
            throw new NoSuchElementException();

        mediaSeries.setId(id);
        return mediaSeriesRepository.save(mediaSeries);
    }

    public void deleteMediaSeries(Long seriesID) throws MediaSeriesNotEmptyException {
        MediaSeries mediaSeries = mediaSeriesRepository.findById(seriesID).orElseThrow(NoSuchElementException::new);
        if(!mediaSeries.getMediumList().isEmpty())
            throw new MediaSeriesNotEmptyException("MediaSeries:"+seriesID+"cannot be deleted as long as it contains Media");

        mediaSeriesRepository.deleteById(seriesID);
    }
}
