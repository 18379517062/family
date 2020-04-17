package com.family.service;

import com.family.entity.user;
import com.family.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class userLoginService {

    @Autowired
    private userMapper usermapper;

    //用户登录
    public user userLogin(String userName,String password){
        return usermapper.userLogin(userName,password);
    }

    //注册新用户
    public int addUser(String userName,String password,String sex ,Date createTime) {
        return usermapper.addUser(userName, password,(sex.equals("男")?true:false),createTime);
    }

    //修改用户信息
    public int modifyUser(String id,
                          String userName,
                          String password,
                          String realName,
                          String sex,
                          String phone,
                          String email,
                          Date birthday,
                          Date updateTime
                          ){
        return usermapper.modifyUser(Integer.parseInt(id),userName,password,realName,(sex.equals("男")?true:false),phone,email,
                birthday,updateTime);
    }


    //查找用户是否存在
    public String selectIsName(String userName){
        return usermapper.selectIsName(userName);
    }

    //查询用户列表
    public List<Map<String,Object>> queryAllUser(){
        return usermapper.queryAllUser();
    }
}
