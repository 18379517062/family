package com.family.mapper;

import com.family.entity.category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface categoryMapper {

    //添加新分类(
    int addCategory(@Param("categoryName") String categoryName, @Param("status") String status);

    //查找类别是否存在
    String selectIsCategory(@Param("categoryName") String categoryName,@Param("status") String status);

    //查找单个类别
    category findById(category category);

    //查找所有类别
    List<category> findCategoryByStatus(@Param("status")String status);

    //按名称搜索类别
    List<category> findCategoryByName(@Param("categoryName")String categoryName);

    //按所给条件查询
    List<category> searchCategory(String status,String categoryName);
    //删除类别
    void delete(category category);
    //修改类别
    int updateCategory(category category);

    int insertSelective(category record);



    int updateByPrimaryKeySelective(category record);

    int updateByPrimaryKey(category record);



//    void delete(category category);
}