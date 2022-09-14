package com.ntg.sadmin.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntg.sadmin.data.entities.SaLoginUserHistory;

public interface SaLoginUserHistoryRepository extends JpaRepository<SaLoginUserHistory, Long> {

}
