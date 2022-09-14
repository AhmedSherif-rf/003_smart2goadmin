package com.ntg.sadmin.data.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "sa_group")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "Group_ID")
public class Group extends AuditEntity implements Serializable {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "sa_group_s", sequenceName = "sa_group_s", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sa_group_s")
    @Column(name = "recid", nullable = false)
    public Long Group_ID;

    @Column(name = "Name")
    public String Name;

    @Column(name = "Email")
    public String Email;

    @Column(name = "Description")
    public String Description;

    @Column(name = "STATUSID")
    public Long Status_ID;

    @Column(name = "STATUS_NAME")
    public String statusName;

    @Column(name = "GroupPrimeID")
    public Long GroupPrimeID;

    @Column(name = "gruop_type")
    public Long gruop_type;

    @Column(name = "PARENT_GROUP_ID")
    public Long PARENT_GROUP_ID;

    @Column(name = "CREATED_BY_NAME")
    public String CREATED_BY_NAME;

    @Column(name = "CREATED_BY_COMPANY")
    public String CREATED_BY_COMPANY;

    @Column(name = "cc_list")
    public String ccList;

    @Column(name = "LAST_MODIFIED_BY_NAME")
    public String LAST_MODIFIED_BY_NAME;

    @Column(name = "CREATION_DATE")
    public Timestamp CREATION_DATE;

    @Column(name = "MODIFICATION_DATE")
    public Timestamp MODIFICATION_DATE;

    @Column(name = "isdefault", nullable = true)
    public Boolean isDefault;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH,
                    CascadeType.PERSIST})
    @JoinTable(
            name = "sa_user_group",
            joinColumns = {@javax.persistence.JoinColumn(name = "groupID")},
            inverseJoinColumns = {@javax.persistence.JoinColumn(name = "userid")}
    )
    @NotFound(action = NotFoundAction.IGNORE)
    public List<Employee> groupUsers = new ArrayList<Employee>();


    @Column(name = "is_have_admin_prev", nullable = true)
    public Boolean isHaveAdminPrev;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PARENT_GROUP_ID", referencedColumnName = "recid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Group parentGroup;

}
