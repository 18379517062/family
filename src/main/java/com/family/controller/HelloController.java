package com.family.controller;

import com.family.entity.Product;
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

    @RequestMapping(value = {"/springboot"})
    public String hello() {
        return "hello";
    }


    @RequestMapping("/index")
    public String myDemo(){

        return "index";
    }

    @RequestMapping("/echarts")
    public String myECharts(Model model){

        String skirt = "裙子";
        int nums = 30;

        model.addAttribute("skirt", skirt);
        model.addAttribute("nums", nums);

        return "echarts";
    }

    @RequestMapping("/project")
    @ResponseBody
    public List<Product> myProject(){

        ArrayList<Product> productArrayList = new ArrayList<Product>();
        Product product1 = new Product();
        product1.setProductName("袜子");
        product1.setNums(15);
        Product product2 = new Product();
        product2.setProductName("羊毛衫");
        product2.setNums(20);
        Product product3 = new Product();
        product3.setProductName("雪纺衫");
        product3.setNums(24);
        Product product4 = new Product();
        product4.setProductName("高跟鞋");
        product4.setNums(30);

        productArrayList.add(product1);
        productArrayList.add(product2);
        productArrayList.add(product3);
        productArrayList.add(product4);

        return productArrayList;
    }

    @RequestMapping("/view")
    public String myView(){
        return "view";
    }
    @RequestMapping("/view1")
    public String myView1(){
        return "view1";
    }
    @RequestMapping("/view2")
    public String myView2(){
        return "view2";
    }

    @RequestMapping("/echartsIncome")
    public String echartsIncome(){
        return "/echarts/echartsIncome";
    }

    @RequestMapping("/echartsIncomes")
    public String echartsIncomes(){
        return "/echarts/echartsIncomes";
    }

}