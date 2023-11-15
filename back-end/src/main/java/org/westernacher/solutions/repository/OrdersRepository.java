package org.westernacher.solutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.westernacher.solutions.domain.entities.Order;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order, Integer>
{
    List<Order> findAllByDeliveredFalse();
    List<Order> findAllByDeliveredTrue();
    int countAllByDeliveredTrue();
    int countAllByDeliveredFalse();
}
