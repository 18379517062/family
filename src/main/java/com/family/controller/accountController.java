package com.family.controller;


import com.family.entity.account;
import com.family.entity.category;
import com.family.entity.user;
import com.family.service.accountService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(value = {"/account"})
@Controller

public class accountController {

    @Autowired
    private accountService accountService;

    //账户管理界面：查找新增页面
    @RequestMapping(value = {"/accountManagePage"})
    public String categoryManagePage(HttpServletRequest request,Model model){
        int userId = ((user)request.getSession().getAttribute("session_user")).getId();
        account account = new account();
        account.setUserId(userId);
        List<account> allAccount = accountService.findAccountByUserId(account);
        model.addAttribute("allAccount",allAccount);

        return "/account/accountManage";
    }

    @ResponseBody
    @PostMapping("/accountDelete")
    public void categoryDelete(account account){
        accountService.delete(account);
    }

    //搜索类别
    @RequestMapping(value = {"/categorySearch"})
    public String categorySearch(Model model,account account) {
        List<account> allAccount = accountService.findAccountByName(account);
        model.addAttribute("allAccount",allAccount);
        return "/account/accountManage";
    }


    //账户新增界面
    @RequestMapping(value = {"/accountEditPage"})
    public String accountEditPage(Model model){
        return "/account/accountEdit";
    }


    @RequestMapping(value = {"/accountEdit"})
    public String accountEdit(HttpServletRequest request,account account, Model model) {
        int userId = ((user)request.getSession().getAttribute("session_user")).getId();
        account.setUserId(userId);

        if(accountService.selectIsAccount(account)!=null) {
            model.addAttribute("message","您已经拥有该账户！");
            return "/account/accountEdit";
        }
        else {
            int res = accountService.addAccount(account);
            if(res == 0) {
                model.addAttribute("message", "出现错误，添加失败！");
                return "/account/accountEdit";
            }
            else{
                model.addAttribute("message","添加成功！");
                return "/account/accountEdit";
            }
        }
    }

}
