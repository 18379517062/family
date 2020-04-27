package com.family.controller;

import com.family.entity.currency;
import com.family.entity.item;
import com.family.entity.user;
import com.family.service.currencyService;
import com.family.service.itemService;
import com.family.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/echarts")
public class echartController {
    @Autowired
    private itemService itemService;
    @Autowired
    private currencyService currencyService;

    private List<currency> currencyList;

    //进入年度报表页面
    @RequestMapping(value = {"/echartsYearPage"})
    public String echartsYearPage(HttpServletRequest request,Model model){
        ArrayList<Integer> yearList = new ArrayList<>();  //年份数组
        yearList = getYear(request);  //获取所有记录的年份
        model.addAttribute("yearList",yearList);

        currencyList = currencyService.findCurrency();   //获取所有汇率
        model.addAttribute("currencyList",currencyList);
        return "/echarts/echartsYear";
    }

    //进入月度报表页面
    @RequestMapping("/echartsMonthPage")
    public String echartsMonthPage(HttpServletRequest request,Model model){
        ArrayList<Integer> yearList = new ArrayList<>();  //年份数组
        yearList = getYear(request);
        model.addAttribute("yearList",yearList);

        currencyList = currencyService.findCurrency();   //获取所有汇率
        model.addAttribute("currencyList",currencyList);
        return "/echarts/echartsMonth";
    }

    //进入每日报表页面
    @RequestMapping("/echartsDayPage")
    public String echartsDayPage(HttpServletRequest request,Model model){
        ArrayList<Integer> yearList = new ArrayList<>();  //年份数组
        yearList = getYear(request);
        model.addAttribute("yearList",yearList);

        currencyList = currencyService.findCurrency();   //获取所有汇率
        model.addAttribute("currencyList",currencyList);
        return "/echarts/echartsDay";
    }

    //进入其他报表页面
    @RequestMapping("/echartsOtherPage")
    public String echartsOtherPage(HttpServletRequest request,Model model){
        ArrayList<Integer> yearList = new ArrayList<>();  //年份数组
        yearList = getYear(request);
        model.addAttribute("yearList",yearList);
        return "/echarts/echartsOther";
    }

    //进入汇率报表页面
    @RequestMapping("/echartsCurrencyPage")
    public String echartsCurrencyPage(HttpServletRequest request,Model model){
        currencyList = currencyService.findCurrency();   //获取所有汇率
        model.addAttribute("currencyList",currencyList);
        return "/echarts/echartsCurrency";
    }


    //
    @RequestMapping("/echartsYear")
    @ResponseBody
    public List<item> echartsYear(HttpServletRequest request){
        item item = new item();
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        item.setUserId(userId);
        List<item> itemList = itemService.findAllItem(item); //收入
        return itemList;
    }

    //年度报表，只要筛查年份
    @RequestMapping("/selectYear")
    @ResponseBody
    public List<item> selectYear(HttpServletRequest request, Model model ,
                                 @RequestParam("year") String year,
                                 @RequestParam("currency") String currency) throws ParseException {
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        List<item> itemList = itemService.findItemByYear(userId,Integer.parseInt(year));



        /**
         * 转换成美元，而有 100欧元 ，转换如下
         * 1、获取 美元/人民币的汇率 = 7.01 ，欧元/人民币的汇率 = 7.41
         * 2、用 7.41去除 7.01 得到 欧元/美元的汇率 = 1.05
         * 3、使用 100*1.05 = 105 ，就是100欧元转换成美元的钱 =105美元
         */
        //对获取的数据进行统一币种化
        int index = Integer.parseInt(currency);
        List<currency> currencyList = currencyService.findCurrency();   //获取所有汇率
        LinkedHashMap<String,Double> map = new LinkedHashMap<>();   //map(currencyName:id)

        for(int i=0;i<currencyList.size();i++) {
            map.put(currencyList.get(i).getCurrencyName() , currencyList.get(i).getRate());
        }
        String name = currencyList.get(index).getCurrencyName();       //要转换成的比重
        double rate = currencyList.get(index).getRate();  //获取下拉框选中的货币的 汇率（对人民币）：美/人 = 7.01
        DecimalFormat df = new DecimalFormat("#.0000"); //保留4位有效数字

        for(int i=0;i<itemList.size();i++){
            double itemRate = map.get(itemList.get(i).getCurrencyName());  //获取当前
            double v =itemRate/ rate;
            double changeMoney =  Double.parseDouble(df.format(itemList.get(i).getMoney() *  v )) ;
            itemList.get(i).setMoney(changeMoney);
            itemList.get(i).setCurrencyName(name);
        }

        return itemList;
    }

    //月度报表，有收入支出，年份两个筛查条件
    @RequestMapping("/selectStatusAndYear")
    @ResponseBody
    public List<item> selectStatusAndYear(HttpServletRequest request, Model model ,
                                          @RequestParam("status") String status,
                                          @RequestParam("year") String year,
                                          @RequestParam("currency") String currency) throws ParseException {
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        List<item> itemList = itemService.findItemByStatusAndYear(userId,status,Integer.parseInt(year));

        /**
         * 转换成美元，而有 100欧元 ，转换如下
         * 1、获取 美元/人民币的汇率 = 7.01 ，欧元/人民币的汇率 = 7.41
         * 2、用 7.41去除 7.01 得到 欧元/美元的汇率 = 1.05
         * 3、使用 100*1.05 = 105 ，就是100欧元转换成美元的钱 =105美元
         */
        //对获取的数据进行统一币种化
        int index = Integer.parseInt(currency);
        List<currency> currencyList = currencyService.findCurrency();   //获取所有汇率
        LinkedHashMap<String,Double> map = new LinkedHashMap<>();   //map(currencyName:id)

        for(int i=0;i<currencyList.size();i++) {
            map.put(currencyList.get(i).getCurrencyName() , currencyList.get(i).getRate());
        }
        String name = currencyList.get(index).getCurrencyName();       //要转换成的比重
        double rate = currencyList.get(index).getRate();  //获取下拉框选中的货币的 汇率（对人民币）：美/人 = 7.01
        DecimalFormat df = new DecimalFormat("#.0000"); //保留4位有效数字

        for(int i=0;i<itemList.size();i++){
            double itemRate = map.get(itemList.get(i).getCurrencyName());  //获取当前
            double v =itemRate/ rate;
            double changeMoney =  Double.parseDouble(df.format(itemList.get(i).getMoney() *  v )) ;
            itemList.get(i).setMoney(changeMoney);
            itemList.get(i).setCurrencyName(name);
        }

        return itemList;
    }

    //汇率报表
    @RequestMapping("/selectCurrency")
    @ResponseBody
    public List<currency> selectCurrency(HttpServletRequest request, Model model ,
                                          @RequestParam("currency") String currency) throws ParseException {
        currencyList = currencyService.findCurrency();   //获取所有汇率

        int index = Integer.parseInt(currency);
        double rate = currencyList.get(index).getRate();
        //保留4位有效数字
        DecimalFormat df = new DecimalFormat("#.0000");
        for(int i=0;i<currencyList.size();i++){
            double v = Double.parseDouble(df.format(currencyList.get(i).getRate() / rate));
            currencyList.get(i).setRate(v);
        }
            return currencyList;
    }


    //下载到Excel
    @RequestMapping("/downloadToExcel")
    public void postCurrencyExcel(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam("currencyIndex") String currencyIndex) throws IOException {

        String name = currencyList.get(Integer.parseInt(currencyIndex)).getCurrencyName();  //获取选中的货币名

        for(int i=0;i<currencyList.size();i++)     //修改名字   如  人民币 -->  人民币 / 美元
            currencyList.get(i).setCurrencyName(currencyList.get(i).getCurrencyName() + " / " + name);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("currencyName", "货币对换");
        fieldMap.put("rate", "对换汇率");

        int sum = currencyList.size();  //记录数

        long currentTime=System.currentTimeMillis(); //获取当前时间时间戳
        String sheetName = "汇率对换表";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename="+currentTime+".xls");//默认Excel名称
        response.flushBuffer();
        OutputStream fos = response.getOutputStream();
        try {
            ExcelUtil.listToExcel(currencyList, fieldMap, sheetName, sum,fos);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //获取记录的所有年份
    public  ArrayList<Integer> getYear(HttpServletRequest request){
        item item = new item();
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        item.setUserId(userId);

        List<item> itemList = itemService.findAllItem(item); //收入
        Set<Integer> yearSet = new HashSet<>();   //存放年份，去重
        Calendar calendar = Calendar.getInstance();

        for(int i = 0;i < itemList.size() ;i++){
            calendar.setTime(itemList.get(i).getCreateTime());	//放入Date类型数据
            int  createYear=  calendar.get(Calendar.YEAR);			//获取年份
            yearSet.add(createYear);//  获取所有年份
        }

        ArrayList<Integer> yearList = new ArrayList<>();  //年份数组
        Iterator<Integer> it = yearSet.iterator();
        while (it.hasNext()) {
            int v =it.next();
            yearList.add(v);
        }
        Collections.reverse(yearList);// 逆序实现从大到小排序
        return yearList;
    }



}
