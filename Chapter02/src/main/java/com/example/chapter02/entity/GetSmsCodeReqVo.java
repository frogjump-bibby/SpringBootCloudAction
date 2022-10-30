package com.example.chapter02.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author joe
 */
@Data
@Builder
public class GetSmsCodeReqVo implements Serializable {

    private String reqId;
    private String mobileNo;

}