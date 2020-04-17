package com.family.mapper;

import com.family.entity.category;
import com.family.entity.item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface itemMapper {
    int deleteByPrimaryKey(Integer id);

    //插入一条记录
    int insertItem(item item);

    //按status查找所有的收支记录
    List<item> findItemByStatus(item item);

    //查找所有的收支记录
    List<item> findAllItem(item item);

    //按所给条件查询收支记录
    List<item> searchItem( @Param("title")String title,
                           @Param("status")String status,
                           @Param("userId")int userId,
                           @Param("categoryId")int categoryId,
                           @Param("startTime") Date startTime,
                           @Param("endTime") Date endTime
                          );

    //删除类别
    void deleteItem(item item);

    int insertSelective(item record);

    item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(item record);

    int updateByPrimaryKey(item record);
}