package com.ntg.sadmin.data.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "sa_loginuser")
public class SaLoginUser implements Serializable {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "sa_loginuser_s", sequenceName = "sa_loginuser_s", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sa_loginuser_s")
    @Column(name = "recid")
    public Long recid;

    @Column(name = "loginUserName")
    public String loginUserName;

    @Column(name = "sessionID")
    public String sessionID;

    @Column(name = "companyName")
    public String companyName;

    @Column(name = "transactionID")
    public String transactionID;

    @Column(name = "userID")
    public String userID;

    @Column(name = "companyId")
    public String companyId;

    @Column(name = "groupCompanyName")
    public String groupCompanyName;

    @Column(name = "login_date")
    public Date loginDate;

    @Column(name = "login_time")
    public Time loginTime = new Time(90 * 1000 * 60 * 60);

    @Column(name = "user_agent")
    public String userAgent;

    @Column(name = "user_ip")
    public String userIp;

    @Column(name = "user_platform")
    public String userPlatform;

}
