package com.erim.bachelor.service;

import com.erim.bachelor.data.InventoryDTO;
import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.MediaSeriesRepository;
import com.erim.bachelor.repositories.MediumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public List<Medium> getAllMedia(){
        return mediumRepository.findAll();
    }

    /**
     * Add new Medium to inventory if id does not exist already otherwise return null
     *
     * @param medium The new Medium to be added
     * @return null or the added Medium
     */
    public Medium addNewMedium(Medium medium){
        if(!mediumRepository.existsById(medium.getMediumID()))
            return mediumRepository.save(medium);
        else {
            return null;
        }
    }

    public Optional<Medium> getMediumById(Long id) {
        return mediumRepository.findById(id);
    }

    public  Optional<Medium> updateMedium(Long id,Medium newMedium) {
        return Optional.of(mediumRepository.findById(id)
                .map(medium -> {
                    medium.setTitle(newMedium.getTitle());

                    return mediumRepository.save(medium);
                })
                .orElseGet(() -> mediumRepository.save(newMedium)));
    }

    public ResponseEntity<String> deleteMedium(Long id){

        Optional<Medium> mediumToDelete = mediumRepository.findById(id);
        if(mediumToDelete.isEmpty())
            return new ResponseEntity<>("Medium with id:"+id+" not found",HttpStatus.BAD_REQUEST);

        Medium medium = mediumToDelete.get();
        if(medium.isBorrowed())
            return new ResponseEntity<>("Medium with Id:"+id+" is still borrowed to User: "+medium.getBorrower().getFullName(),HttpStatus.CONFLICT);
        else {
            mediumRepository.deleteById(id);
            return new ResponseEntity<>("Medium with Id:"+id+" deleted ",HttpStatus.OK);
        }
    }

    public List<Medium> getAllMediaByTitle(String title) {
         MediaSeries mediaSeries = mediaSeriesRepository.findByTitel(title)
                 .orElseThrow(NoSuchElementException::new);

         return mediaSeries.getMediumList();
    }
}
