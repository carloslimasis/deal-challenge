package com.deal.service;

import com.deal.domain.Deal;
import com.deal.exception.NotFoundException;
import com.deal.repository.DealRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;

    public DealServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public Deal createDeal(Deal deal) {
        deal.slugfyDeal();
        return this.dealRepository.save(deal);
    }

    @Override
    public Deal getDealById(String title) {
        return this.dealRepository.findById(title).orElseThrow(() -> new NotFoundException("Deal", "title", title));
    }

    @Override
    public List<Deal> findValidDeals(Date now) {
        return this.dealRepository.findValidDeals(now);
    }

    @Override
    public void deleteDeal(String title) {
        this.dealRepository.delete(getDealById(title));
    }

}
