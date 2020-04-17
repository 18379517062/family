package com.family.controller;

import com.family.entity.Product;
import com.family.entity.item;
import com.family.entity.user;
import com.family.service.itemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class echartController {
    @Autowired
    private itemService itemService;


    @RequestMapping("/income")
    @ResponseBody
    public List<item> income(HttpServletRequest request){
        item item = new item();
        int userId = ((user) request.getSession().getAttribute("session_user")).getId();
        item.setUserId(userId);
        item.setStatus("0");
        List<item> itemList = itemService.findItemByStatus(item); //收入
//        for (int i=0;i<itemList.size();i++)
//            System.out.println(itemList.get(i).getId()+" "+itemList.get(i).getTitle()+" "+itemList.get(i).getCategory().getCategoryName()+" "+
//                    itemList.get(i).getMoney()+" ");
        return itemList;
    }
}
