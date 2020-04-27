package com.family.mapper;

import com.family.entity.currency;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface currencyMapper {

//     插入汇率数据
    int insertCurrency (currency currency);

//    查找所有汇率信息
    List<currency> findCurrency();



    int deleteByPrimaryKey(Integer id);

    int insert(currency record);

    int insertSelective(currency record);

    currency selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(currency record);

    int updateByPrimaryKey(currency record);
}