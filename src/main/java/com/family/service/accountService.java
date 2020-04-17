package com.family.service;

import com.family.entity.account;
import com.family.mapper.accountMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class accountService {

    @Autowired
    private accountMapper accountmapper;

    //添加新账户
    public int addAccount(account account){
        return accountmapper.addAccount(account);
    }

    //查找类别是否存在
    public String selectIsAccount(account account){
        return accountmapper.selectIsAccount(account);
    }

    //查找所有类别
    public List<account> findAccountByUserId(account account){
        return accountmapper.findAccountByUserId(account);
    }

    //按名称搜索类别
    public List<account> findAccountByName(account account){
        return  accountmapper.findAccountByName(account);
    }

    //删除类别
    public  void delete(account account){
        accountmapper.delete(account);
    }

    //修改账户余额
    public int updateAccountMoney(account account){
        return accountmapper.updateAccountMoney(account);
    }
}
