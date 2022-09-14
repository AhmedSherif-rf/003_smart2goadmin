package com.ntg.sadmin.data.entities;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Configuration Entity
 * <p>
 * Used to load the configuration we need from the database
 *
 * @author mashour@ntgclarity.com
 */
@Entity
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "CONFIGURATION")
public class Configuration extends AuditEntity {

    /**
     * The id of the table
     */
    @Id
    @SequenceGenerator(allocationSize = 1, name = "configuration_s", sequenceName = "configuration_s", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "configuration_s")
    @Column(name = "ID")
    private Long id;

    /**
     * The key in the configuration table
     */
    @Column(name = "KEY")
    private String key;

    /**
     * The value in the configuration table
    */
    @Column(name = "VALUE",length = 4000)
    private String value;

    /**
     * The note in the configuration table
     */
    @Column(name = "NOTE")
    private String note;

    @Column(name = "is_global_tenant")
    @ColumnDefault("'0'")
    private boolean isGlobalTenant;

    /**
     * <p>
     * This the getter of the id
     * </p>
     *
     * @return the id of the record
     * @since 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * <p>
     * This the setter of the id
     * </p>
     *
     * @since 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>
     * This the getter of the key
     * </p>
     *
     * @return the key value
     * @since 2020
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>
     * This the setter of the key
     * </p>
     *
     * @since 2020
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * <p>
     * This the getter of the value
     * </p>
     *
     * @return the value
     * @since 2020
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>
     * This the setter of the value
     * </p>
     *
     * @since 2020
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * <p>
     * This the getter of the note
     * </p>
     *
     * @return the value
     * @since 2020
     */
    public String getNote() {
        return note;
    }

    /**
     * <p>
     * This the setter of the note
     * </p>
     *
     * @since 2020
     */
    public void setNote(String note) {
        this.note = note;
    }

    public boolean isGlobalTenant() {
        return isGlobalTenant;
    }

    public void setGlobalTenant(boolean globalTenant) {
        isGlobalTenant = globalTenant;
    }
}
