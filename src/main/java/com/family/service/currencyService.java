package com.family.service;


import com.family.entity.currency;
import com.family.mapper.currencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class currencyService {

    @Autowired
    private currencyMapper currencymapper;


    //    插入汇率数据
    public int insertCurrency(currency currency){
        return currencymapper.insertCurrency(currency);
    }

    //    查找所有汇率信息
    public List<currency> findCurrency(){
        return currencymapper.findCurrency();
    }
}
