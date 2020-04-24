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
        return "/echarts/echartsYear";
    }

    //进入月度报表页面
    @RequestMapping("/echartsMonthPage")
    public String echartsMonthPage(HttpServletRequest request,Model model){
        ArrayList<Integer> yearList = new ArrayList<>();  //年份数组
        yearList = getYear(request);
        model.addAttribute("yearList",yearList);
        return "/echarts/echartsMonth";
    }

    //进入每日报表页面
    @RequestMapping("/echartsDayPage")
    public String echartsDayPage(HttpServletRequest request,Model model){
        ArrayList<Integer> yearList = new ArrayList<>();  //年份数组
        yearList = getYear(request);
        model.addAttribute("yearList",yearList);
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



//    @RequestMapping("/income")
//    @ResponseBody
//    public List<item> income(HttpServletRequest request,item item){
//        if(item.getStatus() == null)
//            item.setStatus("1");
//        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
//        item.setUserId(userId);
//        List<item> itemList = itemService.findItemByStatus(item); //收入
//        return itemList;
//    }

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
    public List<item> selectYear(HttpServletRequest request, Model model ,@RequestParam("year") String year) throws ParseException {
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        List<item> itemList = itemService.findItemByYear(userId,Integer.parseInt(year));
        return itemList;
    }

    //月度报表，有收入支出，年份两个筛查条件
    @RequestMapping("/selectStatusAndYear")
    @ResponseBody
    public List<item> selectStatusAndYear(HttpServletRequest request, Model model ,
                                          @RequestParam("status") String status,
                                          @RequestParam("year") String year
                                          ) throws ParseException {
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        List<item> itemList = itemService.findItemByStatusAndYear(userId,status,Integer.parseInt(year));

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
