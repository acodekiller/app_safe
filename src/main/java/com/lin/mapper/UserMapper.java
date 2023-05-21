package com.lin.mapper;

import com.lin.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description:
 * author: Code_Lin
 * date:2023/5/20 15:39
 */
@Mapper
public interface UserMapper {

    User selectUser(@Param("username") String username,@Param("password") String password);

    User selectUser2(@Param("username") String username,@Param("password") String password);

    List<User> selectUserByOrder(@Param("order") String order);

}
