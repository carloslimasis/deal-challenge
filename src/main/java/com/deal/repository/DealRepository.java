package com.deal.repository;

import com.deal.domain.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DealRepository extends JpaRepository<Deal, String> {
    @Query("SELECT d FROM Deal d WHERE :now >= d.publishDate AND :now <= d.endDate")
    List<Deal> findValidDeals(@Param("now") Date now);
}
