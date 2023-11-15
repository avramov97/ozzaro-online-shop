package org.westernacher.solutions.controllers;

import org.modelmapper.ModelMapper;
import org.westernacher.solutions.domain.entities.Log;
import org.westernacher.solutions.domain.entities.Operation;
import org.westernacher.solutions.domain.models.additional.Logs;
import org.westernacher.solutions.domain.models.view.AllUsersUserViewModel;
import org.westernacher.solutions.service.LogService;
import org.westernacher.solutions.service.UserRoleService;
import org.westernacher.solutions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/organization", consumes = "application/json", produces = "application/json")
public class OrganizationController
{
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final LogService logService;

    @Autowired
    public OrganizationController(LogService logService, UserService userService, UserRoleService userRoleService, ModelMapper modelMapper, LogService logService1)
    {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.logService = logService1;
    }


    @RequestMapping(method = RequestMethod.GET)
    public Set<AllUsersUserViewModel> organization() {
        Set<AllUsersUserViewModel> allUsers =
                this.userService.getAll()
                        .stream()
                        .map(x -> {
                            AllUsersUserViewModel currentUserViewModel = this.modelMapper
                                    .map(x, AllUsersUserViewModel.class);

                            currentUserViewModel.setRole(x.extractAuthority());

                            return currentUserViewModel;
                        })
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        return allUsers;
    }

    @PostMapping("/promote")
    public ResponseEntity promoteUser(@RequestParam(name = "id") String id, Authentication authentication)
    {
        boolean resultOfPromoting = this.userService.promoteUser(id);

        Log log = Logs.setupLog(authentication, "Organization", Operation.Promote);
        this.logService.insertLog(log);

        if(resultOfPromoting) {
            return ResponseEntity.ok().body("User promoted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failure promoting user!");
        }
    }

    @PostMapping("/demote")
    public ResponseEntity demoteUser(@RequestParam(name = "id") String id, Authentication authentication) {
        boolean resultOfDemoting = this.userService.demoteUser(id);

        Log log = Logs.setupLog(authentication, "Organization", Operation.Demote);
        this.logService.insertLog(log);

        if(resultOfDemoting) {
            return ResponseEntity.ok("User demoted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failure demoting user!");
        }
    }
}
