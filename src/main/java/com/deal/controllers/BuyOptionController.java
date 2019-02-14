package com.deal.controllers;

import com.deal.domain.BuyOption;
import com.deal.service.BuyOptionService;
import com.deal.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;

@Controller
@RequestMapping("/buy-options")
public class BuyOptionController {

    @Autowired
    private DealService dealService;

    @Autowired
    private BuyOptionService service;

    @RequestMapping("/new")
    public ModelAndView newBuyOption(BuyOption buyOption) {
        final ModelAndView mv = new ModelAndView("add-buy-option");
        mv.addObject("deals", dealService.findValidDeals(Date.from(Instant.now())));
        mv.addObject("buyOption", buyOption);
        return mv;
    }

    @RequestMapping("/save")
    public ModelAndView save(@Valid BuyOption buyOption, BindingResult result) {
        if (result.hasErrors()) {
            return newBuyOption(buyOption);
        }

        this.service.createBuyOption(buyOption);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/delete/{title}")
    public ModelAndView delete(@PathVariable("title") String title) {
        this.service.deleteBuyOption(title);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit/{title}")
    public ModelAndView edit(@PathVariable("title") String title) {
        final BuyOption buyOption = this.service.getBuyOptionById(title);
        return newBuyOption(buyOption);
    }
}
