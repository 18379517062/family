package com.family.mapper;
import com.family.entity.user;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface userMapper {

    //用户登录
    user userLogin(user user);

    //注册新用户(
    int addUser(@Param("userName") String userName, @Param("password") String password,@Param("sex") String sex,@Param("createTime") Date createTime
               );

    //用户信息更新
    int modifyUser(@Param("id")int id,
                   @Param("userName")String userName,
                   @Param("password") String password,
                   @Param("realName") String realName,
                   @Param("sex") String sex,
                   @Param("phone") String phone,
                   @Param("email") String email,
                   @Param("birthday") Date birthday,
                   @Param("updateTime") Date updateTime);

    //查找用户是否存在 by userName
    String findUserByName(@Param("userName") String userName);

    //查找用户是否存在 by Id
    user findUserById(user user);

    //查询用户列表
    List<Map<String,Object>> queryAllUser();


}