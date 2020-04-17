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
    user userLogin(@Param("userName") String userName, @Param("password") String password);

    //注册新用户(
    int addUser(@Param("userName") String userName, @Param("password") String password,@Param("sex") boolean sex,@Param("createTime") Date createTime
               );

    //用户信息更新
    int modifyUser(@Param("id")int id,
                   @Param("userName")String userName,
                   @Param("password") String password,
                   @Param("realName") String realName,
                   @Param("sex") boolean sex,
                   @Param("phone") String phone,
                   @Param("email") String email,
                   @Param("birthday") Date birthday,
                   @Param("updateTime") Date updateTime);

    //查找用户是否存在
    String selectIsName(@Param("userName") String userName);


    //查询用户列表
    List<Map<String,Object>> queryAllUser();


    int deleteByPrimaryKey(Integer id);

    int insert(user record);

    int insertSelective(user record);

    user selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(user record);

    int updateByPrimaryKey(user record);
}