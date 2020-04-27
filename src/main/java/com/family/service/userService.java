package com.family.service;

import com.family.entity.user;
import com.family.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class userService {

    @Autowired
    private userMapper usermapper;

    //用户登录
    public user userLogin(user user){
        return usermapper.userLogin(user);
    }

    //注册新用户
    public int addUser(String userName,String password,String sex ,Date createTime) {
        return usermapper.addUser(userName, password,sex,createTime);
    }

    //修改用户信息
    public int modifyUser(int id,
                          String userName,
                          String password,
                          String realName,
                          String sex,
                          String phone,
                          String email,
                          Date birthday,
                          Date updateTime
                          ){
        return usermapper.modifyUser(id,userName,password,realName,sex,phone,email,
                birthday,updateTime);
    }

    //修改面膜
    public int changePassword(user user){
        return usermapper.changePassword(user);
    }


    //查找用户是否存在 by userName
    public String findUserByName(String userName){
        return usermapper.findUserByName(userName);
    }

    //查找用户是否存在 by Id
    public user findUserById(user user){
        return usermapper.findUserById(user);
    }

    //查询用户列表
    public List<Map<String,Object>> queryAllUser(){
        return usermapper.queryAllUser();
    }
}
