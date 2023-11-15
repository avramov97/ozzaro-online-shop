package org.westernacher.solutions.controllers;

import org.modelmapper.ModelMapper;
import org.westernacher.solutions.domain.entities.Log;
import org.westernacher.solutions.domain.entities.Operation;
import org.westernacher.solutions.domain.models.additional.Logs;
import org.westernacher.solutions.domain.models.binding.UserEditBindingModel;
import org.westernacher.solutions.domain.models.binding.UserRegisterBindingModel;
import org.westernacher.solutions.domain.models.service.UserServiceModel;
import org.westernacher.solutions.domain.models.view.AllUsersUserFullViewModel;
import org.westernacher.solutions.repository.RoleRepository;
import org.westernacher.solutions.service.LogServiceImpl;
import org.westernacher.solutions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users", consumes = "application/json", produces = "application/json")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final LogServiceImpl logService;

    @Autowired
    public UserController(RoleRepository roleRepository, UserService userService, ModelMapper modelMapper, LogServiceImpl logService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.logService = logService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegisterBindingModel userRegisterBindingModel, Authentication authentication) throws URISyntaxException {

        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Error: Passwords do not match!");
        }

        boolean result = this.userService
                .createUser(this.modelMapper
                        .map(userRegisterBindingModel, UserServiceModel.class));

        if(result == false) {
            return ResponseEntity.badRequest().body("Error: Username already exists. Please try with another one.");
        }

        Log log = Logs.setupLog(authentication, "Users", Operation.Create);
        this.logService.insertLog(log);

        return ResponseEntity.created(new URI("/users/register")).body(result);
    }

    @PostMapping("/edit")
    public ResponseEntity edit(@RequestBody UserEditBindingModel userEditBindingModel, Authentication authentication) throws URISyntaxException {

        boolean result = this.userService.editUser(userEditBindingModel);

        if(result == false) {
            return ResponseEntity.badRequest().body("Error: Account not available.");
        }

        Log log = Logs.setupLog(authentication, "Users", Operation.Edit);
        this.logService.insertLog(log);

        return ResponseEntity.created(new URI("/users/edit")).body(result);
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteUser(@RequestParam(name = "username") String username, Authentication authentication) throws URISyntaxException {
        boolean result = this.userService.deleteUser(username);

        Log log = Logs.setupLog(authentication, "Users", Operation.Delete);
        this.logService.insertLog(log);

        return ResponseEntity.created(new URI("/users/delete")).body(result);
    }

    @GetMapping(value = "/all")
    public Set<AllUsersUserFullViewModel> all()
    {

        Set<AllUsersUserFullViewModel> allUsers =
                this.userService.getAll()
                        .stream()
                        .map(x -> {
                            AllUsersUserFullViewModel currentUserViewModel = this.modelMapper
                                    .map(x, AllUsersUserFullViewModel.class);

                            currentUserViewModel.setRole(x.extractAuthority());

                            return currentUserViewModel;
                        })
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        return allUsers;
    }

}
