package com.deal.repo;

import com.deal.model.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {
    Application getByApplicationId(Long application_id);
}
