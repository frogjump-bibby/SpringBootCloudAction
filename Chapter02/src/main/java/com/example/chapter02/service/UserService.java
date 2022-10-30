package com.example.chapter02.service;

import com.example.chapter02.entity.*;

/**
 * @author joe
 */
public interface UserService {

    //獲取短信驗證碼
    boolean getSmsCode(GetSmsCodeReqVo getSmsCodeReqVo);

    //短信登錄
    LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo) throws BizException;

    //登錄退出
    boolean loginExit(LoginExitReqVo loginExitReqVo);

}