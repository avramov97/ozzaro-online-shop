package org.westernacher.solutions.service;

import org.westernacher.solutions.domain.entities.Order;
import org.westernacher.solutions.domain.models.service.OrderServiceModel;

import java.util.List;

public interface OrderService
{
    List<OrderServiceModel> getAllOrdersByDelivered(boolean delivered);
    Order addOrder(OrderServiceModel orderServiceModel) throws Exception;
    boolean setDeliver(int id, boolean deliver);
    boolean removeOrder(int id);
    boolean removeSelectedOrders(List<Integer> selectedOrders);
    int countWaitingOrders();
    int countDeliveredOrders();
}
