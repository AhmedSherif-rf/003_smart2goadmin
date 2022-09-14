package com.ntg.sadmin.data.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntg.sadmin.data.entities.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Configuration Repository
 * <p>
 * Used to communicate with database to load information from configuration
 * table using spring data
 *
 * @author mashour@ntgclarity.com
 */
@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {

    /**
     * <p>
     * This function is used to load configuration object with key
     * </p>
     *
     * @param key will Query the database to get the object for that key
     * @return Configuration Object
     * @since 2019
     */
    @Transactional
    @Query(value = "select * from CONFIGURATION where KEY = :key and company_name = :companyName and rownum = 1", nativeQuery = true)
    Configuration getByKeyOracle(@Param("key") String key, @Param("companyName") String companyName);


    @Transactional
    @Query(value = "select * from CONFIGURATION where KEY = :key and company_name = :companyName limit 1", nativeQuery = true)
    Configuration getByKeyPostgres(@Param("key") String key, @Param("companyName") String companyName);

    /**
     * <p>
     * This function is used to load configuration object with keys
     * </p>
     *
     * @param keys will Query the database to get the objects for that keys
     * @return Configuration Object
     * @since 2019
     */
    @Transactional
    @Query(value = "select * from CONFIGURATION where KEY in(:keys) and company_name = :companyName order by id asc ", nativeQuery = true)
    List<Configuration> getByKeyIn(@Param("keys") List<String> keys, @Param("companyName") String companyName);


    Configuration save(Configuration configurations);


    <S extends Configuration> Iterable<S> saveAll(Iterable<S> configurations);

    @Transactional
    @Query(value = "select * from CONFIGURATION where company_name = :companyName order by id asc ", nativeQuery = true)
    List<Configuration> findAllByOrderByIdAsc(@Param("companyName") String companyName);

    @Transactional
    @Query(value = "select * from CONFIGURATION where company_name = :companyName ", nativeQuery = true)
    List<Configuration> findAll(@Param("companyName") String companyName);

    @Modifying
    @Transactional
    @Query(value = "delete from CONFIGURATION where company_name = :companyName", nativeQuery = true)
    void deleteAll(@Param("companyName") String companyName);

    @Transactional
    @Query(value = "select * from CONFIGURATION where is_global_tenant = '1'", nativeQuery = true)
    List<Configuration> findByGlobalTenant();

}
