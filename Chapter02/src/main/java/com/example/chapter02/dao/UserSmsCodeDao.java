package com.example.chapter02.dao;

import com.example.chapter02.entity.UserSmsCode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author joe
 */
@Mapper
public interface UserSmsCodeDao {

    int insert(UserSmsCode userSmsCode);

    UserSmsCode selectByMobileNo(String mobileNo);
}
