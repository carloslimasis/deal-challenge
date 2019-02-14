package com.deal.repository;

import com.deal.domain.BuyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BuyOptionRepository extends JpaRepository<BuyOption, String> {
    @Query("SELECT b FROM BuyOption b WHERE :now >= b.startDate AND :now <= b.endDate")
    List<BuyOption> findValidBuyOptions(@Param("now") Date now);

    @Query("SELECT b FROM BuyOption b WHERE deal_id = :deal_id")
    List<BuyOption> findBuyOptionsByDeal(@Param("deal_id") String title);
}
