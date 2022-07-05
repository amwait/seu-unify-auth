package com.seu.seuauth.bean;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InitLoginParamBean {
    private String sumbitUrl;
    private String pwdDefaultEncryptSalt;
    private String lt;
    private String dllt;
    private String execution;
}
