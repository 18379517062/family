package com.family.controller;

import com.alibaba.fastjson.JSON;
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

    private List<item> itemList;


    //进入记录新增页面
    @RequestMapping(value = {"/itemEditPage"})
    public String itemEditPage(Model model, HttpServletRequest request) {
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        account account = new account();
        account.setUserId(userId);
        List<account> allAccount = accountService.findAccountByUserId(account);
        List<currency> allCurrency = currencyService.findCurrency();
        List<category> allCategory = categoryService.findCategoryByStatus("0");
        model.addAttribute("account", allAccount);
        model.addAttribute("currency", allCurrency);
        model.addAttribute("category", allCategory);
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
            item items = new item();
            items.setUserId(userId);
            List<item> list = itemService.findAllItem(items);
            model.addAttribute("item", list);
            return "/item/itemManage";
        }
    }

    //进入收支记录管理界面
    @RequestMapping(value = {"/itemManagePage"})
    public String itemManagePage(Model model, HttpServletRequest request) {

        item item = new item();
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        item.setUserId(userId);

//        item.setUser((user) request.getSession().getAttribute("session_user"));


        itemList = itemService.findAllItem(item);
        model.addAttribute("item", itemList);
        model.addAttribute("itemCount", itemList.size());   //记录数

        return "/item/itemManage";
    }

    //下拉框级联，status：categoryId
    @ResponseBody
    @RequestMapping(value = "/selectState", produces = "application/json;charset=utf-8")
    public String managerList(String status) {

        //调用查询集团下酒店店长信息及其角色信息集合方法
        List<category> list = categoryService.findCategoryByStatus(status);
        ;
//        int lines = hotelChainService.selectManagerCount(map);

        //调用查询集团下酒店店长信息及其角色信息总数方法
        Map<String, Object> returnMap = new HashMap<String, Object>();

        //根据判断结果返回不同结果集
        if (list.size() != 0) {
            returnMap.put("categoryList", list);
            returnMap.put("returnCode", "000000");
            returnMap.put("returnMsg", "获取到数据");
        } else {
            returnMap.put("returnCode", "111111");
            returnMap.put("returnMsg", "没有获取到数据");
        }
        return JSON.toJSONString(returnMap);
    }
    //更加条件查询收支记录
    @RequestMapping(value = "/searchItem")
    public String searchItem(Model model, HttpServletRequest request,
                             @RequestParam("status") String status,
                             @RequestParam("title") String title,
                             @RequestParam("categoryId") String categoryId,
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
//        itemList = itemService.searchItem(title,status,userId,cid,sTime,eTime);
        model.addAttribute("item", itemList);
        model.addAttribute("itemCount", itemList.size());   //记录数
        return "/item/itemManage";

    }

//    @ResponseBody是作用在方法上的，@ResponseBody 表示该方法的返回结果直接写入 HTTP response body 中，一般在异步获取数据时使用【也就是AJAX】。
    @ResponseBody
    @PostMapping("/deleteItem")
    public void deleteItem(item item){
        itemService.deleteItem(item);
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


}
