package com.family.controller;


import com.family.service.currencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = {"/currency"})
@Controller
public class currencyController {

    @Autowired
    private currencyService currencyService;


}
