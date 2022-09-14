package com.ntg.sadmin.web.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ntg.sadmin.web.dto.ConfigurationDTO;
import com.ntg.sadmin.data.entities.Configuration;
import com.ntg.sadmin.web.services.AdminConfigurationService;


@RestController
@RequestMapping(value = "/config", produces = "application/json")
public class AdminConfigrationController {

    @Autowired
    AdminConfigurationService adminConfigService;

    @RequestMapping(value = "/getAllConfig", method = RequestMethod.GET)
    public List<Configuration> getAllConfig() {
        return adminConfigService.getAllConfig();
    }

    @RequestMapping(value = "/readSadminConfig", method = RequestMethod.GET)
    public List<ConfigurationDTO> readSadminConfigrution() throws Exception {
        return adminConfigService.readPropertiesFile();
    }

    @RequestMapping(value = "/storeSadmintFile", method = RequestMethod.POST)
    public void storeTheNewPropertiyFile(@RequestBody List<ConfigurationDTO> newProps) throws Exception {
        adminConfigService.storeTheNewPropertiyFile(newProps,"application_sadmin.properties");
    }

    @RequestMapping(value = "/restoreConfigration", method = RequestMethod.POST)
    public List<Configuration> restoreConfigration(@RequestBody List<Configuration> configuration) {
        return adminConfigService.RestorConfigration(configuration);
    }


    @RequestMapping(value = "/getConfigByKeyIn", method = RequestMethod.POST)
    public List<Configuration> getConfigByKeyIn(@RequestBody List<String> keys) {
        return adminConfigService.getConfigByKeyIn(keys);
    }

}
