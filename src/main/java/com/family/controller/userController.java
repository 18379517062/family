package com.family.controller;

import com.family.entity.user;
import com.family.service.userService;

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
public class userController {

    @Autowired
    private userService userService;

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
    public String userLogin(user user, HttpServletRequest request,Model model){

//        if(request.getSession().getAttribute("session_user")!=null){   //已经有登录
//
//            model.addAttribute("logined","您已经登录，请先退出");

        user user2 = userService.userLogin(user);

        if(user != null){                                                  //登录成功
            model.addAttribute("user",user2);
            user session_user = new user(user2.getId(),user2.getUserName()); //session 存放用户信息：id和userName

            request.getSession().setAttribute("session_user",session_user);     //将用户信息放入session  用于后续的拦截器
            return "/user/userManage";
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

    //修改用户信息
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
        int res = userService.modifyUser(Integer.parseInt(id),userName,password,realName,sex,phone,email,bir,new Date());
        if(res == 0) {
            model.addAttribute("message", "出现错误，修改失败！");
            return "/user/userManage";  //修改失败
        }else {
            model.addAttribute("message","修改成功");
            //session修改
            request.getSession().removeAttribute("session_user");   //先移除
            user user = new user(Integer.parseInt(id),userName);
            request.getSession().setAttribute("session_user",user); //再添加
            user user2 = userService.findUserById(user);   //再查询所有信息by id
            model.addAttribute("user",user2);           //传回去
            return "/user/userManage";      //修改成功
        }
    }


    //跳转到用户注册页面
    @RequestMapping(value = {"/registerPage"})
    public String registerPage(){
        return "register";
    }


    //用户注册
    @RequestMapping(value = {"/register"})
    public String addUser(@RequestParam("userName") String userName,
                          @RequestParam("password") String password,
                          @RequestParam("password2") String password2,
                          @RequestParam("sex") String sex,
                          Model model){
        //查找用户是否存在
        if(userService.findUserByName(userName)!=null) {
            model.addAttribute("error","该用户名已被注册！");
            return "register";
        }
        else if(!password.equals(password2)){
            model.addAttribute("error","两次密码不相同，注册失败！！");
            return "register";
        }
        else {
            Date data = new Date();
            int res = userService.addUser(userName,password,sex,new Date());
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

        return userService.queryAllUser();
    }

    @RequestMapping(value = {"/forgetPage"})
    public  String forgetPage(){
        return "forget";
    }

}
