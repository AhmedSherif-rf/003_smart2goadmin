package com.ntg.sadmin.data.repositories;

import com.ntg.sadmin.data.entities.SaLoginUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SaLoginUserRepository extends JpaRepository<SaLoginUser, Long> {
    SaLoginUser findBySessionID(String id);
    @Transactional
    void deleteBySessionID(String id);
}
