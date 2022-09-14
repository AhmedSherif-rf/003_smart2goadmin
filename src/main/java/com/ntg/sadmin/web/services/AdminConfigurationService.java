package com.ntg.sadmin.web.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.ntg.sadmin.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ntg.sadmin.data.entities.Configuration;
import com.ntg.sadmin.data.repositories.ConfigurationRepository;
import com.ntg.sadmin.web.dto.ConfigurationDTO;

@Service
public class AdminConfigurationService {

    @Autowired
    ConfigurationRepository configurationRepo;

    public List<Configuration> getAllConfig() {
        List<Configuration> config = configurationRepo.findAllByOrderByIdAsc(TenantContext.getCompanyName());
        return config;
    }


    public List<ConfigurationDTO> readPropertiesFile() throws Exception {
        String PropertyFileName = "application_sadmin.properties";
        Properties properties = new Properties();
        List<ConfigurationDTO> config = new ArrayList<>();
        String path = System.getProperty("user.home") + "/.Smart2GoConfig/." + PropertyFileName;
        File file = new File(path);
        FileInputStream fileInput = new FileInputStream(file);
        properties.load(fileInput);
        fileInput.close();
        for (Object key : properties.keySet()) {
            ConfigurationDTO configuDto = new ConfigurationDTO();
            configuDto.setKey(key.toString());
            configuDto.setValue(properties.get(key));
            config.add(configuDto);
        }
        return config;
    }


    public void storeTheNewPropertiyFile(List<ConfigurationDTO> newProps, String propertyFileName) throws Exception {
        InputStream input = AdminConfigurationService.class.getClassLoader().getResourceAsStream(propertyFileName);
        Properties propsToWrite = new Properties();
        String path = System.getProperty("user.home") + "/.Smart2GoConfig/." + propertyFileName;
        File f = new File(path);
        FileInputStream fileInput = new FileInputStream(f);
        propsToWrite.load(fileInput);
        StoreTheNewPropertiyFile(newProps, propertyFileName, f);
        Object[] keys = propsToWrite.keySet().toArray();
        for (Object k : keys) {
            String v = (String) propsToWrite.get(k);
            System.setProperty((String) k, v);
        }
    }

    public static void StoreTheNewPropertiyFile(List<ConfigurationDTO> propsToWrite, String PropertyFileName, File f) throws Exception {
        InputStream in = AdminConfigurationService.class.getClassLoader().getResourceAsStream(PropertyFileName);
        Scanner inp = new Scanner(in);
        FileWriter wr = new FileWriter(f);
        while (inp.hasNextLine()) {
            String line = inp.nextLine().trim();
            if (line.indexOf("=") > 0 && line.startsWith("#") == false) {
                String[] list = line.split("=");
                //find value from the propert
                String Key = list[0].trim();
                ConfigurationDTO newProp = propsToWrite.stream().filter(item ->
                        item.getKey().equals(Key)
                ).findAny().orElse(new ConfigurationDTO());
                Object newValue = newProp.getValue();
                line = Key + "=" + (newValue == null || newValue.equals("null") ? "" : newValue);
            }
            wr.write(line);
            wr.write("\r\n");
        }
        wr.close();
        inp.close();
        in.close();
    }


    public List<Configuration> getConfigByKeyIn(List<String> keys) {
        return configurationRepo.getByKeyIn(keys, TenantContext.getCompanyName());
    }


    public List<Configuration> RestorConfigration(List<Configuration> configration) {
        List<Configuration> config = (List<Configuration>) configurationRepo.saveAll(configration);
        return config;
    }


}
