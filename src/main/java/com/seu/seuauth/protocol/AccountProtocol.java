package com.seu.seuauth.protocol;

import com.seu.seuauth.bean.InitLoginParamBean;
import com.seu.seuauth.bean.InitLoginResponseBean;
import com.seu.seuauth.util.CookieUtil;
import com.seu.seuauth.util.SeuEncryptUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class AccountProtocol {
    @Autowired
    public WebClient webClient;
    public InitLoginResponseBean getLoginWebPage(String serviceUrl) {
        String loginUrl = "https://newids.seu.edu.cn/authserver/login";

        loginUrl += serviceUrl != null? "?service=" +serviceUrl:"";
        HashMap<String,String> cookieMap = new HashMap<>();
        String response = webClient.get().uri(loginUrl).exchange().flatMap(clientResponse -> {
            cookieMap.putAll(CookieUtil.cookie2Map(clientResponse.headers().header("Set-Cookie")));
            return Mono.from(clientResponse.bodyToMono(String.class));
        }).block();

        Document document = Jsoup.parse(Objects.requireNonNull(response));
        Element loginform = document.selectFirst("form[id='casLoginForm']");
        //获取一些必要的登陆参数
        String sumbitUrl = loginform.attr("action");
        String pwdDefaultEncryptSalt = loginform.selectFirst("input[id='pwdDefaultEncryptSalt']").attr("value");
        String lt = loginform.selectFirst("input[name='lt']").attr("value");
        String dllt = loginform.selectFirst("input[name='dllt']").attr("value");
        String execution = loginform.selectFirst("input[name='execution']").attr("value");

        InitLoginParamBean initLoginParamBean = InitLoginParamBean.builder().dllt(dllt).execution(execution).lt(lt).pwdDefaultEncryptSalt(pwdDefaultEncryptSalt).sumbitUrl(sumbitUrl).build();
        return InitLoginResponseBean.builder().initLoginParamBean(initLoginParamBean).cookieMap(cookieMap).build();
    }
    public String login(String account,String pwd,String serviceUrl){
        InitLoginResponseBean initLoginResponseBean = getLoginWebPage(serviceUrl);
        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        params.add("username", account);
        params.add("password", SeuEncryptUtil.encryptAES(pwd,initLoginResponseBean.getInitLoginParamBean().getPwdDefaultEncryptSalt()));
        params.add("lt", initLoginResponseBean.getInitLoginParamBean().getLt());
        params.add("dllt", initLoginResponseBean.getInitLoginParamBean().getDllt());
        params.add("execution", initLoginResponseBean.getInitLoginParamBean().getExecution());
        params.add("_eventId", "submit");
        params.add("rmShown", "1");

        ClientResponse clientResponse = webClient.post().uri("https://newids.seu.edu.cn"+initLoginResponseBean.getInitLoginParamBean().getSumbitUrl())
                .header(HttpHeaders.COOKIE,CookieUtil.getCookieStr(initLoginResponseBean.getCookieMap()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue(params)
                .exchange().block();

        initLoginResponseBean.getCookieMap().putAll(CookieUtil.cookie2Map(clientResponse.headers().header("Set-Cookie")));
        List<String> locationUrlList = clientResponse.headers().header("Location");
        while (locationUrlList!=null&&locationUrlList.size() >0){
            locationUrlList = getLoginTicket(locationUrlList.get(0),initLoginResponseBean);
        }
        return CookieUtil.getCookieStr(initLoginResponseBean.getCookieMap());
    }

    public List<String> getLoginTicket(String redirectUrl,InitLoginResponseBean initLoginResponseBean){
        ClientResponse clientResponse = webClient.get().uri(redirectUrl).header(HttpHeaders.COOKIE,CookieUtil.getCookieStr(initLoginResponseBean.getCookieMap())).exchange().block();
        initLoginResponseBean.getCookieMap().putAll(CookieUtil.cookie2Map(clientResponse.headers().header("Set-Cookie")));
        return clientResponse.headers().header("Location");
    }

    @PostConstruct
    public void test(){
        String dailyReportServiceUrl = "http://ehall.seu.edu.cn/qljfwapp2/sys/lwReportEpidemicSeu/index.do";
        login("wanna","student1",dailyReportServiceUrl);
    }
}
