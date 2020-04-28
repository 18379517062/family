package com.family.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 这部分是最开始创建项目时，为了测试项目能否正常运行写的一个测试
 * <p>
 * 访问http://localhost:8080/hello/springboot     可在页面看见：HelloWord
 */
@Controller
public class HelloController {



    @RequestMapping("/index")
    public String myDemo(){

        return "index";
    }



    @RequestMapping("/view1")
    public String myView1(){
        return "view1";
    }


}