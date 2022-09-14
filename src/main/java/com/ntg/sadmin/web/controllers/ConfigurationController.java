package com.ntg.sadmin.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntg.sadmin.data.service.ConfigurationEntityService;
import com.ntg.sadmin.web.response.StringResponse;

@RestController
@RequestMapping(path = "configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationEntityService configurationEntityService;

    @GetMapping(path = "clear")
    public ResponseEntity<?> clearCache() {

        configurationEntityService.evictGetByKey();
        return new ResponseEntity<>(new StringResponse("Configuration Cache Clear Successful"), HttpStatus.OK);

    }

}
