package com.family.mapper;
import com.family.entity.account;
import com.family.entity.category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface accountMapper {

    //添加新账户(
    int addAccount(account account);

    //查找类别是否存在
    String selectIsAccount(account account);

    //查找所有类别
    List<account> findAccountByUserId(account account);


    //按名称搜索类别
    List<account> findAccountByName(account account);

    //删除类别
    void delete(account account);

    //修改账户余额
    int updateAccountMoney(account account);

    int insert(account record);

    int insertSelective(account record);

    account selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(account record);

    int updateByPrimaryKey(account record);
}