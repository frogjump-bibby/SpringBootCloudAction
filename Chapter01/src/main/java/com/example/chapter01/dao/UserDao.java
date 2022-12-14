package com.example.chapter01.dao;

import com.example.chapter01.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author joe
 */
@Mapper
public interface UserDao {

    String TABLE_NAME = " user ";
    String ALL_FIELDS = "username,password";


    @Insert("INSERT INTO " + TABLE_NAME + "(" + ALL_FIELDS + ") VALUES (#{username}, #{password})")
    int addUser(User user);

}