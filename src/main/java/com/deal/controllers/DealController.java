package com.deal.controllers;

import com.deal.domain.BuyOption;
import com.deal.domain.Deal;
import com.deal.domain.TypeDeal;
import com.deal.service.BuyOptionService;
import com.deal.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/deals")
public class DealController {

    @Autowired
    private DealService service;

    @Autowired
    private BuyOptionService buyOptionService;

    @RequestMapping("/new")
    public ModelAndView newDeal(Deal deal) {
        ModelAndView mv = new ModelAndView("add-deal");
        final List<BuyOption> buyOptions = deal.isNew() ? buyOptionService.getAllBuyOptions() : buyOptionService.findBuyOptionsByDeal(deal.getTitle());
        mv.addObject("buyOptions", buyOptions);
        mv.addObject("typeDeals", TypeDeal.values());
        mv.addObject("deal", deal);
        return mv;
    }

    @RequestMapping("/save")
    public ModelAndView save(@Valid Deal deal, BindingResult result) {
        if (result.hasErrors()) {
            return newDeal(deal);
        }

        this.service.createDeal(deal);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/delete/{title}")
    public ModelAndView delete(@PathVariable("title") String title) {
        this.service.deleteDeal(title);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit/{title}")
    public ModelAndView edit(@PathVariable("title") String title) {
        final Deal dealById = this.service.getDealById(title);
        return newDeal(dealById);
    }

    @RequestMapping("/buy-options/buy/{title}")
    public ModelAndView buy(@PathVariable("title") String title) {
        final BuyOption buyOption = this.buyOptionService.sellUnity(title);
        return newDeal(buyOption.getDeal());
    }
}
