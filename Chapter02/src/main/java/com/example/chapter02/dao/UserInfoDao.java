package com.example.chapter02.dao;


import com.example.chapter02.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author joe
 */
@Mapper
public interface UserInfoDao {

    UserInfo selectByMobileNo(String mobikeNo);

    int insert(UserInfo userInfo);

    int updateById(UserInfo userInfo);
}
