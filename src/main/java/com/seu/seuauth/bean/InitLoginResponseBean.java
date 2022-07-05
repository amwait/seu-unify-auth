package com.seu.seuauth.bean;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Builder
@Data
public class InitLoginResponseBean {
    private InitLoginParamBean initLoginParamBean;
    private HashMap<String,String> cookieMap;
}
