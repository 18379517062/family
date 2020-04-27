package com.family.controller;

import com.family.entity.*;
import com.family.service.accountService;
import com.family.service.categoryService;
import com.family.service.currencyService;
import com.family.service.itemService;
import com.family.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping(value = {"/item"})
@Controller
public class itemController {

    @Autowired
    private itemService itemService;
    @Autowired
    private currencyService currencyService;
    @Autowired
    private accountService accountService;
    @Autowired
    private categoryService categoryService;

    private List<item> itemList;  //用于导出到Excel使用

    //进入记录新增页面
    @RequestMapping(value = {"/itemEditPage"})
    public String itemEditPage(Model model, HttpServletRequest request) {
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        account account = new account();
        account.setUserId(userId);
        List<account> allAccount = accountService.findAccountByUserId(account);
        List<currency> allCurrency = currencyService.findCurrency();
        model.addAttribute("account", allAccount);
        model.addAttribute("currency", allCurrency);
        return "/item/itemEdit";
    }

    //新增一条收支记录
    @RequestMapping(value = {"/itemEdits"})
    @Transactional  //事务注解，出现意外进行回滚
    public String itemEdits(Model model, HttpServletRequest request,
                            @RequestParam("status") String status,
                            @RequestParam("title") String title,
                            @RequestParam("money") String money,
                            @RequestParam("categoryId") String categoryId,
                            @RequestParam("currencyName") String currencyName,
                            @RequestParam("accountId") String accountId,
                            @RequestParam("place") String place,
                            @RequestParam("comment") String comment
    ) {
        item item = new item();
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        item.setUserId(userId);
        item.setCreateTime(new Date());
        item.setStatus(status);
        item.setTitle(title);
        item.setMoney(Double.parseDouble(money));
        item.setCurrencyName(currencyName);
        item.setCategoryId(Integer.parseInt(categoryId));
        item.setAccountId(Integer.parseInt(accountId));
        item.setPlace(place);
        item.setComment(comment);

        account account = new account();
        account.setId(Integer.parseInt(accountId));
        if(status.equals("0")) //收入则余额加
            account.setAccountMoney(Double.parseDouble(money));
          else
            account.setAccountMoney(-Double.parseDouble(money));
        int res = itemService.insertItem(item);  //新增记录
        int res2 = accountService.updateAccountMoney(account); //修改账户余额


        if (res == 0 || res2 == 0) {
            model.addAttribute("message", "出现错误，添加失败！");
            return "/item/itemEdit";
        } else {
            model.addAttribute("message", "添加成功！");

            return "/item/itemManage";
        }
    }

    //进入收支记录管理界面
    @RequestMapping(value = {"/itemManagePage"})
    public String itemManagePage(Model model, HttpServletRequest request) {
        List<currency> currencyList = currencyService.findCurrency();   //获取所有汇率
        model.addAttribute("currencyList",currencyList);
        return "/item/itemManage";
    }

    //下拉框级联，status：categoryId
    @ResponseBody
    @RequestMapping(value = "/selectState")
    public List<category> managerList(@RequestParam("status") String status ) {
        List<category> list = categoryService.findCategoryByStatus(status);
        return list;  //返回对应的category
    }


    @ResponseBody
    @RequestMapping("/deleteItem")
    public  String deleteItem(String id,Model model) {
        item item = new item();
        item.setId(Integer.parseInt(id));
        int result = itemService.deleteItem(item);
        if (result > 0) {
            itemList.remove(new item(Integer.parseInt(id)));  //移除
            return "success";
        }
        return "error";
    }


    @RequestMapping("/downloadToExcel")
    public void postItemExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //导出excel
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("id", "商品id");
        fieldMap.put("title", "商品标题");
        fieldMap.put("userId", "用户名");
        fieldMap.put("status", "记录类型");
        fieldMap.put("category.categoryName", "类别");
        fieldMap.put("account.accountName", "账户名称");
        fieldMap.put("money", "金额");
        fieldMap.put("currencyName", "币种");
        fieldMap.put("createTime", "创建时间");
        fieldMap.put("place", "地点");
        fieldMap.put("comment", "备注");

        int sum = itemList.size();  //记录数

        long currentTime=System.currentTimeMillis(); //获取当前时间时间戳
        String sheetName = "商品管理报表";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename="+currentTime+".xls");//默认Excel名称
        response.flushBuffer();
        OutputStream fos = response.getOutputStream();
        try {
            ExcelUtil.listToExcel(itemList, fieldMap, sheetName, sum,fos);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //更加条件查询收支记录 ,也是页面数据的加载
    @RequestMapping("/change")
    @ResponseBody
    public List<item> change(Model model, HttpServletRequest request,
                            @RequestParam("title") String title,
                            @RequestParam("status") String status,
                             @RequestParam("categoryId") String categoryId,
                             @RequestParam("currency") String currency,
                             @RequestParam(value = "startTime",required = false) String startTime,
                             @RequestParam(value = "endTime",required = false) String endTime) throws ParseException {

        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        int cid = Integer.parseInt(categoryId);

        //开始时间和结束时间转换Date
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date sTime = null;
        Date eTime = null;
        if(startTime=="") {
            startTime = "2010-01-01";
            sTime = format1.parse(startTime);
        }
        else
            sTime = format1.parse(startTime);

        if(endTime=="") {
            eTime = new Date();
        }
        else
            eTime =  format1.parse(endTime);

        itemList =  itemService.searchItem(title,status,userId,cid,sTime,eTime);

        //对获取的数据进行统一币种化
        int index = Integer.parseInt(currency);
        if(index == -1)    //默认设置不变化
            return itemList;
        else{
            /**
             * 转换成美元，而有 100欧元 ，转换如下
             * 1、获取 美元/人民币的汇率 = 7.01 ，欧元/人民币的汇率 = 7.41
             * 2、用 7.41去除 7.01 得到 欧元/美元的汇率 = 1.05
             * 3、使用 100*1.05 = 105 ，就是100欧元转换成美元的钱 =105美元
             */
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
    }

}
