package org.westernacher.solutions.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.westernacher.solutions.domain.entities.Order;
import org.westernacher.solutions.domain.models.binding.OrderCreateBindingModel;
import org.westernacher.solutions.domain.models.service.OrderServiceModel;
import org.westernacher.solutions.domain.models.view.OrdersViewModel;
import org.westernacher.solutions.service.EmailService;
import org.westernacher.solutions.service.OrderService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/orders", consumes = "application/json", produces = "application/json")
public class OrdersController
{
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    int countOrders = 0;

    @Autowired
    public OrdersController(OrderService orderService, ModelMapper modelMapper, EmailService emailService)
    {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @GetMapping(value = "/waiting")
    public List<OrdersViewModel> notDeliveredOrders()
    {
        List<OrdersViewModel> notDeliveredOrders =
                this.orderService.getAllOrdersByDelivered(false)
                        .stream()
                        .map(x -> this.modelMapper.map(x, OrdersViewModel.class))
                        .collect(Collectors.toList());

        return notDeliveredOrders;
    }

    @GetMapping(value = "/delivered")
    public List<OrdersViewModel> deliveredOrders()
    {
        List<OrdersViewModel> deliveredOrders =
                this.orderService.getAllOrdersByDelivered(true)
                        .stream()
                        .map(x -> this.modelMapper.map(x, OrdersViewModel.class))
                        .collect(Collectors.toList());

        return deliveredOrders;
    }

    @PostMapping("/add")
    public ResponseEntity addOrder(@RequestBody OrderCreateBindingModel orderCreateBindingModel) throws Exception
    { 
        Order order = this.orderService.addOrder(this.modelMapper.map(orderCreateBindingModel, OrderServiceModel.class));

        if(order == null) // Is it correct??
        {
            return ResponseEntity.badRequest().body("Извинете, во моментов нема останати дуксери.");
        }

        countOrders++;
        String toOwner = "mihaeladimitrijevska06@abv.bg";
        String subjectOwner = "Нарачка број " + countOrders;
        String textOwner = "Здраво,\n\n" + "Вие треба да испратите нарачка за " + orderCreateBindingModel.getCity() + " потполнувајќи ги следните информации: \n\n"
                            + "Име на блуза: " + order.getSweatshirt().getName() + "\n"
                            + "Цена: " + order.getSweatshirt().getNewPrice() + " денари\n"
                            + "Величина: " + orderCreateBindingModel.getSize() + "\n"
                            + "Адреса: " + orderCreateBindingModel.getAddress() + "\n"
                            + "Град: " + orderCreateBindingModel.getCity() + "\n"
                            + "Телефонски број: " + orderCreateBindingModel.getNumber() + "\n"
                            + "Емаил: " + orderCreateBindingModel.getEmail() + "\n\n\n"
                            + "Поздрав,\n"
                            + "Ozzaro MK";

        
        String subjectClient = "Успешно извршена порачка од Ozzaro MK";
        String textClient = "Здраво,\n\nВие успешно извршивте порачка на блузата "  + order.getSweatshirt().getName() +
                            ", величина " + orderCreateBindingModel.getSize() + ".\n" +
                            "Вашата нарачка ќе биде доставена во рок од 3 дена на адресата " + orderCreateBindingModel.getAddress() + ", "
                            + orderCreateBindingModel.getCity() + "." + " Дополнително очекувајте да се сврзат со Вас на телефонот " + orderCreateBindingModel.getNumber() +
                            ".\n\nПри погрешни информации, Ве замолуваме да ни пишете на овој меил или да се јавите на телефонскиот број 075 930 288.\n" +
                            "Ви благодарим што го одбравте Ozzaro, Вашиот избор е наше задоволство.\n\n\n" +
                            "Срдечен поздрав,\n" +
                            "Тимот на Ozzaro MK" + "\n\n";
                            // attachments (facebook, instagram profiles)

    //   this.emailService.sendSimpleMessage(orderCreateBindingModel.getEmail(), subjectClient, textClient);
    //   this.emailService.sendSimpleMessage("dimitrijevskih@gmail.com", subjectOwner, textOwner);



        return ResponseEntity.created(new URI("/orders/add")).body(true);
    }

    @PostMapping("/deliver")
    public ResponseEntity setOrderDone(@RequestParam(name = "id") int id, Authentication authentication)
    {
        boolean isDone = this.orderService.setDeliver(id, true);

        if(isDone)
        {
            System.out.println("DELIVERED");
            return ResponseEntity.ok().body(true);
        }
        else
        {
            System.out.println("NOT ERROR DELIVERED");
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/remove-delivered-flag")
    public ResponseEntity setOrderNotDone(@RequestParam(name = "id") int id, Authentication authentication)
    {
        boolean isDone = this.orderService.setDeliver(id, false);

        if(isDone)
        {
            return ResponseEntity.ok().body(true);
        }
        else
        {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/remove")
    public ResponseEntity removeOrder(@RequestParam(name = "id") int id, Authentication authentication)
    {
        boolean isDone = this.orderService.removeOrder(id);

        if(isDone)
        {
            return ResponseEntity.ok().body("Order removed successfully!");
        }
        else
        {
            return ResponseEntity.badRequest().body("Order is not removed");
        }
    }

    @PostMapping("/remove-selected")
    public ResponseEntity removeSelectedOrders(@RequestParam(name = "selectedOrders") List<Integer> selectedOrders, Authentication authentication)
    {
        boolean removed = this.orderService.removeSelectedOrders(selectedOrders);

        if(removed == true)
        {
            return ResponseEntity.ok(true);
        }
        else
        {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/count-waiting")
    public int getCountWaitingOrders()
    {
        return this.orderService.countWaitingOrders();
    }

    @GetMapping("count-delivered")
    public int getCountDeliveredOrders()
    {
        return this.orderService.countDeliveredOrders();
    }


}
