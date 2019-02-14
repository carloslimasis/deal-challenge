package com.deal.service;

import com.deal.domain.BuyOption;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BuyOptionService {
    BuyOption createBuyOption(BuyOption buyOption);
    BuyOption getBuyOptionById(String title);
    List<BuyOption> getAllBuyOptions();
    List<BuyOption> findValidBuyOptions(@Param("now") Date now);
    void deleteBuyOption(String title);
    BuyOption sellUnity(String title);
    List<BuyOption> findBuyOptionsByDeal(@Param("deal_id") String title);
}
