package com.deal.service;

import com.deal.domain.BuyOption;
import com.deal.domain.Deal;
import com.deal.domain.TypeDeal;
import com.deal.exception.NotFoundException;
import com.deal.repository.BuyOptionRepository;
import com.deal.repository.DealRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BuyOptionServiceTest {
    private static final Date TODAY = Date.from(Instant.now());

    private BuyOptionService service;
    private DealRepository dealRepository;
    private BuyOptionRepository repository;
    private BuyOption buyOption;
    private Deal deal;

    @Before
    public void setUp() {
        this.repository = mock(BuyOptionRepository.class);
        this.dealRepository = mock(DealRepository.class);
        this.service = new BuyOptionServiceImpl(this.dealRepository, this.repository);
        this.buyOption = BuyOption.builder()
                .title("Frigideira de 20cm")
                .normalPrice(99.9)
                .salePrice(89.9)
                .percentageDiscount(10.0)
                .quantityCupom(5L)
                .startDate(TODAY)
                .endDate(Date.from(TODAY.toInstant().plus(5, ChronoUnit.DAYS)))
                .build();

        this.deal = Deal.builder()
                .title("Frigideira de Alumínio com Revestimento Cerâmico de 20cm, 24cm ou 28cm")
                .text("Frigideira de Alumínio com Revestimento Cerâmico de 20cm, 24cm ou 28cm")
                .createDate(TODAY)
                .endDate(Date.from(TODAY.toInstant().plus(6, ChronoUnit.DAYS)))
                .publishDate(TODAY)
                .type(TypeDeal.PRODUCT)
                .totalSold(0L)
                .build();
    }

    @Test
    public void createBuyOptionSuccessfully() {
        when(this.repository.save(this.buyOption)).thenReturn(this.buyOption);
        BuyOption createdBuyOption = this.service.createBuyOption(this.buyOption);
        verify(this.repository, times(1)).save(this.buyOption);
        assertEquals(this.buyOption.getTitle(), createdBuyOption.getTitle());
        assertThat(createdBuyOption.getSalePrice(), CoreMatchers.is(89.91));
    }

    @Test
    public void findAllSuccessfully() {
        when(this.repository.findAll()).thenReturn(asList(this.buyOption));
        List<BuyOption> buyOptions = service.getAllBuyOptions();
        Assertions.assertThat(buyOptions).contains(this.buyOption);
    }

    @Test
    public void getBuyOptionById() {
        when(this.repository.findById(this.buyOption.getTitle())).thenReturn(of(this.buyOption));
        BuyOption buyOptionFromRepository = service.getBuyOptionById(this.buyOption.getTitle());
        assertEquals(buyOptionFromRepository.getTitle(), this.buyOption.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void getBuyOptionByIdNotFoundException() {
        BuyOption buyOptionFromRepository = service.getBuyOptionById(this.buyOption.getTitle());
        assertEquals(buyOptionFromRepository.getTitle(), this.buyOption.getTitle());
    }

    @Test
    public void deleteBuyOptionSuccessfully() {
        when(this.repository.findById(this.buyOption.getTitle())).thenReturn(of(this.buyOption));
        when(this.repository.findAll()).thenReturn(emptyList());
        doNothing().when(this.repository).delete(Mockito.any());
        service.deleteBuyOption(this.buyOption.getTitle());
        List<BuyOption> buyOptions = service.getAllBuyOptions();
        Assertions.assertThat(buyOptions).isEmpty();
    }

    @Test(expected = NotFoundException.class)
    public void deleteBuyOptionWithNotFoundException() {
        doNothing().when(this.repository).delete(Mockito.any());
        service.deleteBuyOption(this.buyOption.getTitle());
    }

    @Test
    public void findValidBuyOptionsSuccessfully() {
        when(this.repository.findValidBuyOptions(TODAY)).thenReturn(Arrays.asList(this.buyOption));
        List<BuyOption> buyOptions = service.findValidBuyOptions(TODAY);
        Assertions.assertThat(buyOptions).contains(this.buyOption);
    }

    @Test
    public void sellUnityTest() {
        when(this.repository.findById(this.buyOption.getTitle())).thenReturn(of(this.buyOption));
        when(this.repository.save(this.buyOption)).thenReturn(this.buyOption);
        when(this.dealRepository.findById(this.deal.getTitle())).thenReturn(of(this.deal));
        this.buyOption.setDeal(this.deal);
        assertThat(this.buyOption.getQuantityCupom(), CoreMatchers.is(5L));
        this.service.sellUnity(this.buyOption.getTitle());
        verify(this.dealRepository, times(1)).save(this.deal);
        assertThat(this.buyOption.getQuantityCupom(), CoreMatchers.is(4L));
    }

    @Test
    public void findBuyOptionsByDealSuccessfully() {
        when(this.repository.findBuyOptionsByDeal(this.buyOption.getTitle())).thenReturn(Arrays.asList(this.buyOption));
        List<BuyOption> buyOptions = service.findBuyOptionsByDeal(this.buyOption.getTitle());
        Assertions.assertThat(buyOptions).contains(this.buyOption);
    }

}
