package com.deal.controllers;

import com.deal.service.BuyOptionService;
import com.deal.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.Date;

@Controller
public class IndexController {

    @Autowired
    private DealService service;

    @Autowired
    private BuyOptionService buyOptionService;

    @RequestMapping("/")
    public ModelAndView index() {
        final ModelAndView mv = new ModelAndView("index");
        final Date now = Date.from(Instant.now());
        mv.addObject("deals", service.findValidDeals(now));
        mv.addObject("buyOptions", buyOptionService.findValidBuyOptions(now));
        return mv;
    }
}
