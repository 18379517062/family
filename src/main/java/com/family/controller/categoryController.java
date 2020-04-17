package com.family.controller;


import com.family.entity.category;
import com.family.service.categoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping(value = {"/category"})
@Controller
public class categoryController {

    @Autowired
    private categoryService categoryService;


    //类别管理界面：查找新增页面
    @RequestMapping(value = {"/categoryManagePage"})
    public String categoryManagePage(Model model,String id){
        if(id == null)
            id= "1";
        List<category> allCategory = categoryService.findCategoryByStatus(id);
        model.addAttribute("allCategory",allCategory);
        return "/category/categoryManage";
    }

    @RequestMapping(value = {"/categoryManage"})
    public String categoryManage(Model model){
        //List<category> allCategory = categoryService.findCategory("1");


        return "";
    }


    //类别新增界面
    @RequestMapping(value = {"/categoryEditPage"})
    public String categoryEditPage(Model model){
        return "/category/categoryEdit";
    }

    @RequestMapping(value = {"/categoryEdit"})
    public String categoryEdit( @RequestParam("categoryName") String categoryName,
                                @RequestParam("status") String status,
                                Model model){
        if(categoryService.selectIsCategory(categoryName,status)!=null) {
            model.addAttribute("message","该类别已存在！");
            return "/category/categoryEdit";
        }
        else {
            int res = categoryService.addCategory(categoryName, status);
            if(res == 0) {
                model.addAttribute("message", "出现错误，添加失败！");
                return "/category/categoryEdit";
            }
            else{
                model.addAttribute("message","添加成功！");
                return "/category/categoryEdit";
            }
        }
    }

    @RequestMapping(value = {"/categoryChangePage"})
    public String categoryChangePage(Model model,category category) {
        if(category.getId()!= 0){
             category newCategory = categoryService.findById(category);
            model.addAttribute("newCategory",newCategory);
        }
        return "/category/categoryChange";
    }

    @RequestMapping(value = {"/categoryChange"})
    public String categoryChange(Model model,category category) {
        if(categoryService.updateCategory(category) !=0){
//            category newCategory = categoryService.findById(category);
            model.addAttribute("error","修改成功");
            List<category> allCategory = categoryService.findCategoryByStatus("1");
            model.addAttribute("allCategory",allCategory);
            return "/category/categoryManage";
        }
        else {
            model.addAttribute("error","修改失败");
            return "/category/categoryChange";
        }

    }

    @ResponseBody
    @PostMapping("/categoryDelete")
    public void categoryDelete(category category){
        categoryService.delete(category);
//        ResObject<Object> object = new ResObject<Object>(Constant.Code01, Constant.Msg01, null, null);
//        return object;
    }


    //搜索类别
    @RequestMapping(value = {"/searchCategory"})
    public String searchCategory(Model model,
                                 @RequestParam("status") String status,
                                 @RequestParam("categoryName") String categoryName) {
        List<category> allCategory = categoryService.searchCategory(status,categoryName);
        model.addAttribute("allCategory",allCategory);
        model.addAttribute("status",status);
        return "/category/categoryManage";
    }


}
