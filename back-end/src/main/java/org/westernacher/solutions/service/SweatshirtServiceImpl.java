package org.westernacher.solutions.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.westernacher.solutions.domain.entities.Sweatshirt;
import org.westernacher.solutions.domain.models.service.SweatshirtServiceModel;
import org.westernacher.solutions.exceptions.InvalidAverageNumberException;
import org.westernacher.solutions.repository.SweatShirtsRepository;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SweatshirtServiceImpl implements SweatshirtService
{
    private final SweatShirtsRepository sweatShirtsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SweatshirtServiceImpl(SweatShirtsRepository sweatShirtsRepository, ModelMapper modelMapper)
    {
        this.sweatShirtsRepository = sweatShirtsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SweatshirtServiceModel> getAll()
    {
        return this.sweatShirtsRepository
                .findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, SweatshirtServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String updateAvgRating(int id, int rating)
    {
        Sweatshirt sweatshirt = this.sweatShirtsRepository.findById(id).orElse(null);

        if(sweatshirt != null)
        {
            sweatshirt.setTimesRated(sweatshirt.getTimesRated() + 1);
            sweatshirt.setRatingSum(sweatshirt.getRatingSum() + rating);
            this.sweatShirtsRepository.save(sweatshirt);
            DecimalFormat df = new DecimalFormat("####0.00");

            try
            {
                double avgRating = (double) sweatshirt.getRatingSum() / (double) sweatshirt.getTimesRated();

                if(avgRating>0)
                {
                    return df.format(avgRating);
                }
                else
                {
                    throw new InvalidAverageNumberException();
                }

            }
            catch (Exception e)
            {
                System.out.println("Exception on division regarding the average rating");
                return null;
            }
            catch (InvalidAverageNumberException invalidAverageNumberException)
            {
                System.out.println("Invalid number for average number (average number is < 0");
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ResponseEntity updateAvailability(int id, int availability)
    {
        Optional<Sweatshirt> sweatshirt = this.sweatShirtsRepository.findById(id);

        if(sweatshirt.isPresent())
        {
            sweatshirt.get().setrAvailability(availability);
            this.sweatShirtsRepository.save(sweatshirt.get());
            return ResponseEntity.ok().body(true);
        }

        return ResponseEntity.badRequest().body(false);
    }

    @Override
    public boolean addSweatshirt(SweatshirtServiceModel sweatshirtServiceModel)
    {
        Sweatshirt sweatshirt = this.modelMapper.map(sweatshirtServiceModel, Sweatshirt.class);

        try
        {
            this.sweatShirtsRepository.save(sweatshirt);
            return true;
        }
        catch (DataIntegrityViolationException e)
        {
            System.out.println("Error while adding Sweatshirt");
        }
        return false;
    }

    @Override
    public ResponseEntity removeSweatshirt(int id)
    {
        Optional<Sweatshirt> sweatshirt = this.sweatShirtsRepository.findById(id);
        if (sweatshirt.isPresent())
        {
            try
            {
                this.sweatShirtsRepository.delete(sweatshirt.get());
                return ResponseEntity.ok().body(true);
            }
            catch (DataIntegrityViolationException e)
            {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(false);
            }
        }

        return ResponseEntity.badRequest().body(false); // Means that no sweatshirt is found
    }

    @Override
    public Sweatshirt getSweatshirt(int id)
    {
        Sweatshirt sweatshirt = this.sweatShirtsRepository.findById(id).orElse(null);

        if(sweatshirt == null)
        {
            return null;
        }

        return sweatshirt;
    }

    @Override
    public boolean decreaseAvailability(Sweatshirt sweatshirt) throws Exception
    {
        Optional<Sweatshirt> foundSweatshirt = this.sweatShirtsRepository.findById(sweatshirt.getId());

        if(!foundSweatshirt.isPresent())
        {
            return false;
        }

        int availability = foundSweatshirt.get().getrAvailability();

        if (availability - 1 < 0)
        {
            System.out.println("There is no left " + sweatshirt.getName() + " sweatshirts.");
            return false;
//          throw new Exception("Po malku od 0");
        }

        try
        {
            foundSweatshirt.get().setrAvailability(availability - 1);
//            this.sweatShirtsRepository.save(foundSweatshirt);
        }
        catch (DataAccessException ex)
        {
            System.out.println("Problem with Database storing: " + ex);
            return false;
        }

        return true;
    }
}
