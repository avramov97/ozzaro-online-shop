package org.westernacher.solutions.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.westernacher.solutions.domain.entities.Order;
import org.westernacher.solutions.domain.entities.Sweatshirt;
import org.westernacher.solutions.domain.models.service.OrderServiceModel;
import org.westernacher.solutions.repository.OrdersRepository;
import org.westernacher.solutions.repository.SweatShirtsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService
{
    private final OrdersRepository ordersRepository;
    private final SweatShirtsRepository sweatShirtsRepository;
    private final SweatshirtService sweatshirtService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository, SweatShirtsRepository sweatShirtsRepository, SweatshirtService sweatshirtService, ModelMapper modelMapper)
    {
        this.ordersRepository = ordersRepository;
        this.sweatShirtsRepository = sweatShirtsRepository;
        this.sweatshirtService = sweatshirtService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderServiceModel> getAllOrdersByDelivered(boolean delivered)
    {
        List<OrderServiceModel> list;

        if(delivered)
        {
             list = this.ordersRepository.findAllByDeliveredTrue().stream()
                     .map(x -> this.modelMapper.map(x, OrderServiceModel.class))
                     .collect(Collectors.toUnmodifiableList());
        }
        else
        {
            list = this.ordersRepository.findAllByDeliveredFalse().stream()
                    .map(x -> this.modelMapper.map(x, OrderServiceModel.class))
                    .collect(Collectors.toUnmodifiableList());
        }

        return list;
    }

    @Override
    public Order addOrder(OrderServiceModel orderServiceModel) throws Exception
    {
        Order order = this.modelMapper.map(orderServiceModel, Order.class);
        boolean isDecreased = this.sweatshirtService.decreaseAvailability(order.getSweatshirt());

        if (isDecreased == false)
        {
            return null;
            // throw new Exception("Sorry, there is no available" + order.getSweatshirt().getName() + "sweatshirt at the moment.");
        }

        try
        {
            this.ordersRepository.save(order);
            return order;
        }
        catch (DataAccessException exception)
        {
            System.out.println("Data not stored correctly.");
            return null;
        }
    }

    @Override
    public boolean setDeliver(int id, boolean deliver)
    {
        Optional<Order> order = this.ordersRepository.findById(id);
        if(order.isPresent())
        {
            if(deliver)
            {
                order.get().setDelivered(true);
            }
            else
            {
                order.get().setDelivered(false);
            }
            this.ordersRepository.save(order.get());
            return true;
        }

        return false;
    }

    @Override
    public boolean removeOrder(int id)
    {
        Optional<Order> order = this.ordersRepository.findById(id);
        if(order.isPresent())
        {
            this.ordersRepository.delete(order.get());
            return true;
        }

        return false;
    }

    @Override
    public boolean removeSelectedOrders(List<Integer> selectedOrders)
    {
        for(int i : selectedOrders)
        {
            try
            {
               this.ordersRepository.deleteById(i);
            }
            catch (Exception e)
            {
                System.out.println("Eror while deleting.");
                return false;
            }
        }

        return true;
    }

    @Override
    public int countWaitingOrders()
    {
        return this.ordersRepository.countAllByDeliveredFalse();
    }

    @Override
    public int countDeliveredOrders()
    {
        return this.ordersRepository.countAllByDeliveredTrue();
    }


}
