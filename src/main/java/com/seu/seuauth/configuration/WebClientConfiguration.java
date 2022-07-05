package com.seu.seuauth.configuration;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient getWebClient(){

        SslContext sslContext;
        try {
            sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
        SslContext finalSslContext = sslContext;
        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create().keepAlive(true)
                .compress(true).followRedirect(false).secure(sslContextSpec -> sslContextSpec.sslContext(finalSslContext))
//                .tcpConfiguration(tcpClient -> tcpClient.proxy(typeSpec -> typeSpec.type(ProxyProvider.Proxy.HTTP).address(new InetSocketAddress("127.0.0.01",8888))))
                ;
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
}
