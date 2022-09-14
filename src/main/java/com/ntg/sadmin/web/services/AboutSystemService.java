package com.ntg.sadmin.web.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
public class AboutSystemService {
    @Value("${pom.version}")
    public String sadminVersion;

    public String getSAdminVersion() {
        return sadminVersion;
    }

}
