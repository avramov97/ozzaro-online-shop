package org.westernacher.solutions.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.westernacher.solutions.domain.entities.Order;
import org.westernacher.solutions.domain.entities.SavedOrder;
import org.westernacher.solutions.domain.entities.User;
import org.westernacher.solutions.domain.models.binding.SavedOrderBindingModel;
import org.westernacher.solutions.domain.models.service.SavedOrderServiceModel;
import org.westernacher.solutions.domain.models.view.AllSweatShirtsViewModel;
import org.westernacher.solutions.domain.models.view.SavedOrdersViewModel;
import org.westernacher.solutions.service.LogServiceImpl;
import org.westernacher.solutions.service.SweatshirtService;
import org.westernacher.solutions.service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/", consumes = "application/json", produces = "application/json")
@CrossOrigin(origins = "*")
public class HomeController
{
    private final SweatshirtService sweatShirtService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final LogServiceImpl logService;

    @Autowired
    public HomeController(SweatshirtService sweatShirtService, UserService userService, ModelMapper modelMapper, LogServiceImpl logService)
    {
        this.sweatShirtService = sweatShirtService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.logService = logService;
    }

    @GetMapping("/hello")
//    @ResponseBody
    public String hello()
    {
        return "Hello World";
    }


    @GetMapping
    public List<AllSweatShirtsViewModel> all()
    {
        List<AllSweatShirtsViewModel> allSweatShirts =
                this.sweatShirtService.getAll()
                        .stream()
                        .map(x -> this.modelMapper.map(x, AllSweatShirtsViewModel.class))
                        .collect(Collectors.toList());

        return allSweatShirts;
    }

   @GetMapping("/cart")
   public List<SavedOrdersViewModel> cart(@RequestParam(name = "username") String username)
   {
       System.out.println(username);
       List<SavedOrder> listSavedOrders = this.userService.getUserCart(username);

       List<SavedOrdersViewModel> list = this.userService.getUserCart(username)
               .stream()
               .map(x -> this.modelMapper.map(x, SavedOrdersViewModel.class))
               .collect(Collectors.toList());

       return list;
       //       return this.userService.getUserCart(username);
   }

   @GetMapping("/cart-size")
   public int getCartSize(@RequestParam(name = "username") String username)
   {
       System.out.println("Cart size: " + this.userService.getUserCartSize(username));
       return this.userService.getUserCartSize(username);
   }

    @GetMapping("/waiting-size")
    public int getWaitingOrdersSize()
    {
        return this.userService.getWaitingOrdersSize();
    }


    @GetMapping("/delivered-size")
    public int getDeliveredOrdersSize()
    {
        return this.userService.getDeliveredOrdersSize();
    }

    @PostMapping("/save-order")
    public ResponseEntity saveOrderToCart(@RequestBody SavedOrderBindingModel savedOrderBindingModel)
    {
        //return this.userService.saveOrder(this.modelMapper.map(savedOrderBindingModel, SavedOrderServiceModel.class));
        return this.userService.saveOrder(savedOrderBindingModel);
    }

    @PostMapping("/delete-saved-order")
    public ResponseEntity deleteSavedOrder(@RequestParam(name = "id") String id)
    {
        return this.userService.removeSavedOrder(id);
    }


}
