package com.deal.repo;

import com.deal.model.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {
    Optional<Application> getByApplicationId(Long application_id);
}
