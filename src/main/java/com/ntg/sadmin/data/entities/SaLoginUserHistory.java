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
@Table(name = "sa_loginuser_history")
public class SaLoginUserHistory implements Serializable {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "sa_loginuser_history_s", sequenceName = "sa_loginuser_history_s", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sa_loginuser_history_s")
    @Column(name = "recid", nullable = false)
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
    public Time loginTime;

    @Column(name = "logout_date")
    public Date logoutDate;

    @Column(name = "logout_time")
    public Time logoutTime = new Time(90 * 1000 * 60 * 60);

}
