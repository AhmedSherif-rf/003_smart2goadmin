package com.ntg.sadmin.data.entities;

import com.ntg.sadmin.TenantContext;
import com.ntg.sadmin.utils.Utils;
import com.ntg.sadmin.web.dto.CRMSessionInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity<T> {

    @Column(name = "company_name", nullable = false, updatable = false)
    @ColumnDefault("'NTG'")
    private String companyName;

    @PrePersist
    public void prePersist() {
        this.companyName = TenantContext.getCompanyName();
    }

    @PreUpdate
    public void preUpdate() {
        this.companyName = TenantContext.getCompanyName();
    }
}
