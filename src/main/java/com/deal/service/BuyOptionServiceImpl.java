package com.deal.service;

import com.deal.domain.BuyOption;
import com.deal.domain.Deal;
import com.deal.exception.NotFoundException;
import com.deal.repository.BuyOptionRepository;
import com.deal.repository.DealRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BuyOptionServiceImpl implements BuyOptionService {

    private final DealRepository dealRepository;
    private final BuyOptionRepository buyOptionRepository;

    public BuyOptionServiceImpl(DealRepository dealRepository, BuyOptionRepository buyOptionRepository) {
        this.dealRepository = dealRepository;
        this.buyOptionRepository = buyOptionRepository;
    }

    @Override
    public BuyOption createBuyOption(BuyOption buyOption) {
        buyOption.calculeSalesPrice();
        return this.buyOptionRepository.save(buyOption);
    }

    @Override
    public BuyOption getBuyOptionById(String title) {
        return this.buyOptionRepository.findById(title).orElseThrow(() -> new NotFoundException("BuyOption", "title", title));
    }

    @Override
    public List<BuyOption> getAllBuyOptions() {
        return this.buyOptionRepository.findAll();
    }

    @Override
    public List<BuyOption> findValidBuyOptions(Date now) {
        return this.buyOptionRepository.findValidBuyOptions(now);
    }

    @Override
    public void deleteBuyOption(String title) {
        this.buyOptionRepository.delete(this.getBuyOptionById(title));
    }

    @Override
    public BuyOption sellUnity(String title) {
        final BuyOption buyOption = this.buyOptionRepository.findById(title)
                .orElseThrow(() -> new NotFoundException("BuyOption", "title", title));

        buyOption.sellUnity();

        final BuyOption editedBuyOption = this.buyOptionRepository.save(buyOption);
        final Deal dealById = this.dealRepository.findById(editedBuyOption.getDeal().getTitle())
                .orElseThrow(() -> new NotFoundException("Deal", "title", title));

        dealById.plusTotalSold();

        this.dealRepository.save(dealById);

        return editedBuyOption;
    }

    @Override
    public List<BuyOption> findBuyOptionsByDeal(String title) {
        return this.buyOptionRepository.findBuyOptionsByDeal(title);
    }

}
