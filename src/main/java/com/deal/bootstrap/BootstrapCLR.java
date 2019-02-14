package com.deal.bootstrap;

import com.deal.domain.BuyOption;
import com.deal.domain.Deal;
import com.deal.domain.TypeDeal;
import com.deal.service.BuyOptionService;
import com.deal.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;

@Component
public class BootstrapCLR implements CommandLineRunner {

    private final DealService dealService;
    private final BuyOptionService buyOptionService;

    @Autowired
    public BootstrapCLR(DealService dealService, BuyOptionService buyOptionService) {
        this.dealService = dealService;
        this.buyOptionService = buyOptionService;
    }

    @Override
    public void run(String... args) throws Exception {
        final Date today = Date.from(Instant.now());

        final BuyOption buyOption = BuyOption.builder()
                .title("Frigideira de 20cm")
                .normalPrice(99.9)
                .salePrice(89.9)
                .percentageDiscount(10.0)
                .quantityCupom(5L)
                .startDate(today)
                .endDate(Date.from(today.toInstant().plus(5, ChronoUnit.DAYS)))
                .build();

        System.out.println(buyOptionService.createBuyOption(buyOption));

        final Deal deal = Deal.builder()
                .title("Frigideira de Alumínio com Revestimento Cerâmico de 20cm, 24cm ou 28cm")
                .text("Frigideira de Alumínio com Revestimento Cerâmico de 20cm, 24cm ou 28cm")
                .createDate(today)
                .endDate(Date.from(today.toInstant().plus(6, ChronoUnit.DAYS)))
                .publishDate(today)
                .type(TypeDeal.PRODUCT)
                .totalSold(0L)
                .buyOptions(new HashSet<>(buyOptionService.getAllBuyOptions()))
                .build();

        System.out.println(dealService.createDeal(deal));
    }
}
