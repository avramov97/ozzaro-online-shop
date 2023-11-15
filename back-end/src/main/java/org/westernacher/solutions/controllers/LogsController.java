package org.westernacher.solutions.controllers;

import org.modelmapper.ModelMapper;
import org.westernacher.solutions.domain.models.view.AllLogsLogViewModel;
import org.westernacher.solutions.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@RequestMapping(value = "/logs", consumes = "application/json", produces = "application/json")
@RequestMapping("/logs")
public class LogsController
{
    private final LogService logService;
    private final ModelMapper modelMapper;

    @Autowired
    public LogsController(LogService logService, ModelMapper modelMapper) {
        this.logService = logService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/all", produces = "application/json")
    public List<AllLogsLogViewModel> getLogs()
    {

        return this
                .logService
                .getAll()
                .stream()
                .map(x -> this.modelMapper.map(x, AllLogsLogViewModel.class))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/delete", produces = "application/json")
//    @PreAuthorize("hasAuthority('ROOT-ADMIN')")
    public ResponseEntity<Boolean> deleteLogs(@RequestParam("logs" ) List<String> list) throws URISyntaxException {
        this.logService.removeLogsById(list);

        return ResponseEntity.created(new URI("/users/delete")).body(true);
    }

    @PostMapping("/delete/all")
    @PreAuthorize("hasAuthority('ROOT-ADMIN')")
    public String deleteAllLogs()
    {
        this.logService.removeAllLogs();

        return "redirect:/logs/all";
    }

}
