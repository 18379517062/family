package com.family.util;


import com.family.entity.currency;
import com.family.service.currencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

@Component
public class currencyInsert {
    private static final Logger logger = LoggerFactory.getLogger(currencyInsert.class);

    @Autowired
    private currencyService currencyService;

    @Scheduled(cron = "0 * * * * *")
    public void clearDataJob() {
        logger.info("---------定时任务开始执行---------" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        insertCurrency(currencyService);
        logger.info("---------定时任务执行成功---------" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    /**
     * 清理数据
     *
     * @param currencyService
     */
    private static void insertCurrency(currencyService currencyService) {

        try {
            //获取汇率数据
            LinkedHashMap<String, Double> map = currencyRate.getRate();
            for (String key : map.keySet()) {
                currency currency = new currency();
                currency.setCurrencyName(key);
                currency.setRate(map.get(key)+0.0);
                System.out.println("key= "+ key + " and value= " + map.get(key));

                int res = currencyService.insertCurrency(currency);//调用service层的方法 插入到数据库
                if(res == 1){
                    System.out.println("插入"+ key + " ：" + map.get(key));
                }
            }


        } catch (Exception e) {
            logger.error("更新失败，失败原因：" + e.getMessage());
        }
    }
}
