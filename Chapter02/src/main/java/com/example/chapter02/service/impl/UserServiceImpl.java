package com.example.chapter02.service.impl;

import com.example.chapter02.dao.UserInfoDao;
import com.example.chapter02.dao.UserSmsCodeDao;
import com.example.chapter02.entity.*;
import com.example.chapter02.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author joe
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    final
    UserSmsCodeDao userSmsCodeDao;

    final
    UserInfoDao userInfoDao;

    final
    RedisTemplate redisTemplate;

    public UserServiceImpl(UserSmsCodeDao userSmsCodeDao, UserInfoDao userInfoDao, RedisTemplate redisTemplate) {
        this.userSmsCodeDao = userSmsCodeDao;
        this.userInfoDao = userInfoDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean getSmsCode(GetSmsCodeReqVo getSmsCodeReqVo) {
        //隨機生成6位短信驗證碼
        String smsCode = String.valueOf((int) (Math.random() * 100000 + 1));
        //todo 調用短信平台接口
        //存儲用戶短信驗證碼信息至短信驗證碼信息表
        UserSmsCode userSmsCode = UserSmsCode.builder().mobileNo(getSmsCodeReqVo.getMobileNo()).smsCode(smsCode)
                .sendTime(new Timestamp(new Date().getTime())).createTime(new Timestamp(new Date().getTime()))
                .build();
        userSmsCodeDao.insert(userSmsCode);
        return true;
    }

    @Override
    public LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo) throws BizException {
        //1、驗證短信驗證碼是否正確
        UserSmsCode userSmsCode = userSmsCodeDao.selectByMobileNo(loginByMobileReqVo.getMobileNo());
        if (userSmsCode == null || !userSmsCode.getSmsCode().equals(loginByMobileReqVo.getSmsCode())) {
            throw new BizException(-1, "驗證碼輸入錯誤");
        }
        //2、判斷用戶是否註冊
        UserInfo userInfo = userInfoDao.selectByMobileNo(loginByMobileReqVo.getMobileNo());
        if (userInfo == null) {
            //隨機生成用戶ID
            String userId = String.valueOf((int) (Math.random() * 100000 + 1));
            userInfo = UserInfo.builder().userId(userId).mobileNo(loginByMobileReqVo.getMobileNo()).isLogin("1")
                    .loginTime(new Timestamp(new Date().getTime())).createTime(new Timestamp(new Date().getTime()))
                    .build();
            //完成系統默認註冊流程
            userInfoDao.insert(userInfo);
        } else {
            userInfo.setIsLogin("1");
            userInfo.setLoginTime(new Timestamp(new Date().getTime()));
            userInfoDao.updateById(userInfo);
        }
        //3、生成用戶會話信息
        String accessToken = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        //將用戶會話信息存儲至Redis服務
        redisTemplate.opsForValue().set("accessToken", userInfo, 30, TimeUnit.DAYS);
        //4、封裝響應參數
        return LoginByMobileResVo.builder().userId(userInfo.getUserId())
                .accessToken(accessToken).build();
    }

    @Override
    public boolean loginExit(LoginExitReqVo loginExitReqVo) {
        try {
            redisTemplate.delete(loginExitReqVo.getAccessToken());
            return true;
        } catch (Exception e) {
            log.error(e + "_" + e);
            return false;
        }
    }
}