package com.family.service;


import com.family.entity.category;
import com.family.entity.item;
import com.family.mapper.categoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class categoryService {

    @Autowired
    private categoryMapper categorymapper;

    //添加新类别  0：收入  /  1：支出
    public int addCategory(String categoryName,String status ) {
        return categorymapper.addCategory(categoryName,status);
    }

    //查找类别是否存在
    public String selectIsCategory(String categoryName,String status){
        return categorymapper.selectIsCategory(categoryName,status);
    }

    //查找单个类别
    public category findById(category category){
        return categorymapper.findById(category);
    }

    //查找所有种类
    public List<category> findCategoryByStatus(String status){
        return categorymapper.findCategoryByStatus(status);
    }


    //按所给条件查询
    public List<category> searchCategory(String status,String categoryName) {
        return categorymapper.searchCategory(status,categoryName);
    }
    //按名字搜索类别
    public List<category> findCategoryByName(String categoryName){
        return categorymapper.findCategoryByName(categoryName);
    }


    //通过类别id删除
    public void delete(category category){
        categorymapper.delete(category);
    }

    //修改类别
    public int updateCategory(category category){
        return  categorymapper.updateCategory(category);
    }

}
