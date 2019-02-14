package com.deal.service;

import com.deal.domain.Deal;

import java.util.Date;
import java.util.List;

public interface DealService {
    Deal createDeal(Deal deal);
    Deal getDealById(String title);
    List<Deal> findValidDeals(Date now);
    void deleteDeal(String title);
}
