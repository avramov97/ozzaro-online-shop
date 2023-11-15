package org.westernacher.solutions.service;

import org.springframework.http.ResponseEntity;
import org.westernacher.solutions.domain.entities.Sweatshirt;
import org.westernacher.solutions.domain.models.service.SweatshirtServiceModel;

import java.util.List;


public interface SweatshirtService
{
    List<SweatshirtServiceModel> getAll();
    String updateAvgRating(int id, int rating);
    ResponseEntity updateAvailability(int id, int availability);
    boolean addSweatshirt(SweatshirtServiceModel sweatshirtServiceModel);
    ResponseEntity removeSweatshirt(int id);
    Sweatshirt getSweatshirt(int id);
    boolean decreaseAvailability(Sweatshirt sweatshirt) throws Exception;
}
