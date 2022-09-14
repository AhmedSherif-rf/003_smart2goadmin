package com.ntg.sadmin.web.controllers;


import com.ntg.sadmin.web.dto.LoginUserInfo;
import com.ntg.sadmin.web.response.StringResponse;
import com.ntg.sadmin.web.services.AboutSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/aboutSystem")
public class AboutSystemController {

    @Autowired
    AboutSystemService aboutSystemService;

    @GetMapping(value = "/getSAdminVersion")
    public StringResponse getSystemVersions() {

        return new StringResponse(aboutSystemService.getSAdminVersion());
    }


}
