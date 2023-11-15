package org.westernacher.solutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.westernacher.solutions.domain.entities.SavedOrder;
import org.westernacher.solutions.domain.entities.User;

public interface SavedOrdersRepository extends JpaRepository<SavedOrder, String>
{
    int countAllByUser(User user);
}

