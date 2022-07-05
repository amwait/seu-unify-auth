package com.seu.seuauth.controller;

import com.seu.seuauth.protocol.AccountProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Scope("prototype")
@ResponseBody
@Controller
@Slf4j
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AccountProtocol accountProtocol;
    @RequestMapping(value="/orderactivity",method = RequestMethod.POST)
    public String getactivitycookie(String studentnum,String pwd) {
        String activityServiceUrl = "http://ehall.seu.edu.cn/gsapp/sys/jzxxtjapp/*default/index.do";
        return accountProtocol.login(studentnum,pwd, activityServiceUrl);

    }
    @RequestMapping(value="/dailyreport",method = RequestMethod.POST)
    public String dailyreport(String studentnum, String pwd) {
        String dailyReportServiceUrl = "http://ehall.seu.edu.cn/qljfwapp2/sys/lwReportEpidemicSeu/index.do";
        return accountProtocol.login(studentnum,pwd,dailyReportServiceUrl);
    }

    @RequestMapping(value="/servicecookie",method = RequestMethod.POST)
    public String serviceCookie(String studentnum, String pwd,String serviceUrl) {
        return accountProtocol.login(studentnum,pwd,serviceUrl);
    }
}
