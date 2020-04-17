package com.family.controller;

import com.family.entity.user;
import com.family.service.userLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

@Controller
@RequestMapping(value = {"/user"})
public class userLoginController {

    @Autowired
    private userLoginService userLoginService;

    @RequestMapping(value = {"/dashboard"})
    public String dashboard(){
        return "dashboard";
    }

    @RequestMapping(value = {"/index"})
    public String index(){
        return "index";
    }

    //登录页面
    @RequestMapping(value = {"/loginPage"})
    public String loginPage(){
        return "login";
    }

    @RequestMapping(value = {"/login"})
    public String userLogin(@RequestParam("userName") String userName,
                            @RequestParam("password") String password,
                            HttpServletRequest request,Model model){

//        if(request.getSession().getAttribute("session_user")!=null){   //已经有登录
//
//            model.addAttribute("logined","您已经登录，请先退出");

        user user = userLoginService.userLogin(userName,password);

        if(user != null){                                                  //登录成功
            request.getSession().setAttribute("session_user",user);     //将用户信息放入session  用于后续的拦截器

            return "/user/userManage";
//            return  "index";
        }
        model.addAttribute("error", "用户名或密码错误，请重新登录！");
        return "login";  //登录失败
    }

    //退出
    @RequestMapping(value = {"/logoutPage"})
    public String logoutPage(HttpServletRequest request){
        request.getSession().removeAttribute("session_user");
        return "index"; //返回主页
    }

    //用户修改信息
    @RequestMapping(value = {"/userManagePage"})
    public String userManagePage(){
        return "user/userManage";
    }

    @RequestMapping(value = {"/userManage"})
    public String modifyUser(@RequestParam("id")String id,
                             @RequestParam("userName") String userName,
                             @RequestParam("password") String password,
                             @RequestParam("realName") String realName,
                             @RequestParam("sex") String sex,
                             @RequestParam("phone") String phone,
                             @RequestParam("email") String email,
                             @RequestParam("birthday") String birthday,
                             HttpServletRequest request, Model model ){
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date bir = null;
        try {
             bir = format1.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int res = userLoginService.modifyUser(id,userName,password,realName,sex,phone,email,bir,new Date());
        if(res == 0) {
            model.addAttribute("error", "出现错误，修改失败！");
            return "/user/userManage";  //修改失败
        }else {
            model.addAttribute("修改成功，请重新登录！");
            return "login";     //修改成功
        }
    }


    //跳转到用户注册页面
    @RequestMapping(value = {"/registerPage"})
    public String registerPage(){
        return "register";
    }


    @RequestMapping(value = {"/register"})
    public String addUser(@RequestParam("userName") String userName,
                          @RequestParam("password") String password,
                          @RequestParam("password2") String password2,
                          @RequestParam("sex") String sex,
                          Model model){
        //查找用户是否存在
        if(userLoginService.selectIsName(userName)!=null) {
            model.addAttribute("error","该用户名已被注册！");
            return "register";
        }
        else if(!password.equals(password2)){
            model.addAttribute("error","两次密码不相同，注册失败！！");
            return "register";
        }
        else {
            Date data = new Date();
            int res = userLoginService.addUser(userName,password,sex,new Date());
            if(res == 0){
                model.addAttribute("error", "出现错误，注册失败！");
                return "register";
            }else {
                model.addAttribute("error", "恭喜您，注册成功！");
                return "login";
            }
        }

    }

    /**
     * 用于测试拦截器（用户是否登录，查看session）
     * 查询用户列表  http://localhost:8080/user/queryAllUser
     * @return 用户列表（json串）
     */
    @ResponseBody
    @RequestMapping(value = {"/queryAllUser"})
    public List<Map<String,Object>> queryAllUser(){

        return userLoginService.queryAllUser();
    }

    @RequestMapping(value = {"/forgetPage"})
    public  String forgetPage(){
        return "forget";
    }

}
