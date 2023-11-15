package org.westernacher.solutions.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.westernacher.solutions.domain.entities.Sweatshirt;
import org.westernacher.solutions.domain.models.binding.SweatshirtAdd;
import org.westernacher.solutions.domain.models.binding.SweatshirtUpdateAvailability;
import org.westernacher.solutions.domain.models.binding.SweatshirtUpdateRating;
import org.westernacher.solutions.domain.models.service.SweatshirtServiceModel;
import org.westernacher.solutions.service.LogServiceImpl;
import org.westernacher.solutions.service.SweatshirtService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
//@CrossOrigin("http://localhost:63343")
@RequestMapping(value = "/sweatshirts", consumes = "application/json", produces = "application/json")
public class SweatshirtsController
{
    private final SweatshirtService sweatshirtService;
    private final ModelMapper modelMapper;  
    private final LogServiceImpl logService;

    @Autowired
    public SweatshirtsController(SweatshirtService sweatshirtService, ModelMapper modelMapper, LogServiceImpl logService)
    {
        this.sweatshirtService = sweatshirtService;
        this.modelMapper = modelMapper;
        this.logService = logService;
    }

    @PostMapping("/change-rating")
    public ResponseEntity partialUpdateSweatshirt(@RequestBody SweatshirtUpdateRating sweatshirtUpdateRating) throws URISyntaxException
    {
        String avgRating = this.sweatshirtService.updateAvgRating(sweatshirtUpdateRating.id, sweatshirtUpdateRating.rating);

        return ResponseEntity.created(new URI("/sweatshirts/change-rating")).body(avgRating);
    }

    @PostMapping("/update-availability")
    public ResponseEntity updateAvailability(@RequestBody SweatshirtUpdateAvailability sweatshirtUpdateAvailability) throws URISyntaxException
    {
        return this.sweatshirtService.updateAvailability(sweatshirtUpdateAvailability.id, sweatshirtUpdateAvailability.availability);
    }

    @PostMapping("/add")
    public ResponseEntity addSweatshirt(@RequestBody SweatshirtAdd sweatshirtAdd) throws URISyntaxException
    {
        boolean result = this.sweatshirtService.addSweatshirt(this.modelMapper
                .map(sweatshirtAdd, SweatshirtServiceModel.class));

        return ResponseEntity.created(new URI("/sweatshirts/add")).body(result);
    }

    @PostMapping("/remove")
    public ResponseEntity removeSweatshirt(@RequestParam(name = "id") int id)
    {
        return this.sweatshirtService.removeSweatshirt(id);
    }

    @GetMapping("/product")
    public Sweatshirt getSingleSweatshirt(@RequestParam(name = "id") int id)
    {
        Sweatshirt sw =  this.sweatshirtService.getSweatshirt(id);
        return sw;
//        return this.sweatshirtService.getSweatshirt(id);
    }


//    @GetMapping("/get-times-rated")
//    public int getTimesRated()
//    {
//        int timesRated = this.sweatshirtService
//
//        List<AllSweatShirtsViewModel> allSweatShirts =
//                this.sweatShirtService.getAll()
//                        .stream()
//                        .map(x -> this.modelMapper.map(x, AllSweatShirtsViewModel.class))
//                        .collect(Collectors.toList());
//
//        return allSweatShirts;
//    }

}