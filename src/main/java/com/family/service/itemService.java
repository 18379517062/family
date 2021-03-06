package com.family.service;

import com.family.entity.item;
import com.family.mapper.itemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class itemService {
    @Autowired
    private itemMapper itemmapper;

    //新增收支记录
    public int insertItem(item item){
        return itemmapper.insertItem(item);
    }

    //按status查找所有的收支记录
    public List<item> findItemByStatus(item item){
        return itemmapper.findItemByStatus(item);
    }

    //查找所有的收支记录
    public List<item> findAllItem(item item){
        return  itemmapper.findAllItem(item);
    }

    //按所给条件查询收支记录
    public List<item> searchItem(String title,
                                 String status,
                                 int userId,
                                 int categoryId,
                                 Date startTime,
                                 Date endTime){
        return itemmapper.searchItem(title,status,userId,categoryId,startTime,endTime);
    }

    //查询某年的数据
    public List<item> findItemByYear(int userId,int year){
        return itemmapper.findItemByYear(userId,year);
    }

    //查询某年某收支类型的数据
    public List<item> findItemByStatusAndYear(int userId,String status,int year){
        return itemmapper.findItemByStatusAndYear(userId,status,year);
    }

    //删除回路
    public int deleteItem(item item){
        return itemmapper.deleteItem(item);
    }

    //查询总数
    public int count(){
        return itemmapper.count();
    }

}
