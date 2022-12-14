package com.example.chapter02.entity;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author joe
 */
@Data
@Builder
public class LoginByMobileResVo implements Serializable {

    private String userId;
    private String accessToken;

}
