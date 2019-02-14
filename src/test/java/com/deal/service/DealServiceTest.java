package com.deal.service;

import com.deal.domain.BuyOption;
import com.deal.domain.Deal;
import com.deal.domain.TypeDeal;
import com.deal.exception.NotFoundException;
import com.deal.repository.DealRepository;
import org.assertj.core.api.Assertions;
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
import static org.mockito.Mockito.*;

public class DealServiceTest {
    private static final Date TODAY = Date.from(Instant.now());

    private DealService service;
    private DealRepository repository;
    private Deal deal;

    @Before
    public void setUp() {
        this.repository = mock(DealRepository.class);
        this.service = new DealServiceImpl(this.repository);
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
    public void createDealSuccessfully() {
        when(this.repository.save(this.deal)).thenReturn(this.deal);
        Deal createdDeal = this.service.createDeal(this.deal);
        verify(this.repository, times(1)).save(this.deal);
        assertEquals(this.deal.getTitle(), createdDeal.getTitle());
        assertEquals(createdDeal.getUrl(), "frigideira-de-aluminio-com-revestimento-ceramico-de-20cm-24cm-ou-28cm");
    }

    @Test
    public void findAllSuccessfully() {
        when(this.repository.findValidDeals(TODAY)).thenReturn(asList(this.deal));
        List<Deal> deals = service.findValidDeals(TODAY);
        Assertions.assertThat(deals).contains(this.deal);
    }

    @Test
    public void getDealById() {
        when(this.repository.findById(this.deal.getTitle())).thenReturn(of(this.deal));
        Deal dealFromRepository = service.getDealById(this.deal.getTitle());
        assertEquals(dealFromRepository.getTitle(), this.deal.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void getDealByIdNotFoundException() {
        Deal dealFromRepository = service.getDealById(this.deal.getTitle());
        assertEquals(dealFromRepository.getTitle(), this.deal.getTitle());
    }

    @Test
    public void deleteDealSuccessfully() {
        when(this.repository.findById(this.deal.getTitle())).thenReturn(of(this.deal));
        when(this.repository.findAll()).thenReturn(emptyList());
        doNothing().when(this.repository).delete(Mockito.any());
        service.deleteDeal(this.deal.getTitle());
        List<Deal> deals = service.findValidDeals(TODAY);
        Assertions.assertThat(deals).isEmpty();
    }

    @Test(expected = NotFoundException.class)
    public void deleteDealWithNotFoundException() {
        doNothing().when(this.repository).delete(Mockito.any());
        service.deleteDeal(this.deal.getTitle());
    }

    @Test
    public void findValidDealsSuccessfully() {
        when(this.repository.findValidDeals(TODAY)).thenReturn(Arrays.asList(this.deal));
        List<Deal> deals = service.findValidDeals(TODAY);
        Assertions.assertThat(deals).contains(this.deal);
    }
}
