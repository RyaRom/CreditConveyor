package com.deal.repo;

import com.deal.model.Entities.Application;
import com.deal.model.Entities.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepo extends JpaRepository<Credit, Long> {
}
