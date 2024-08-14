package com.deal.repo;

import com.deal.model.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {
    @Query("SELECT a FROM Application a WHERE a.application_id = :application_id")
    Application getByApplication_id(@Param("application_id") Long application_id);
}
