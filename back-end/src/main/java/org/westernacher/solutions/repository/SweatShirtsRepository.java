package org.westernacher.solutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.westernacher.solutions.domain.entities.Sweatshirt;

import java.util.Optional;

public interface SweatShirtsRepository extends JpaRepository<Sweatshirt, Integer>
{
    Optional<Sweatshirt> findByName(String s);

    // This was my excercise for PATCH request, but it did not succeed
    @Modifying
    @Query("update Sweatshirt u set u.timesRated = :timesRated where u.id = :id")
    void updateRating(@Param(value = "id") int id, @Param(value = "timesRated") int timesRated);

}
