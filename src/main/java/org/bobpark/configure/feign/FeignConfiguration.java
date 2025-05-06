package org.bobpark.configure.feign;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import org.apache.http.HttpHeaders;

import com.google.common.collect.Maps;

import feign.RequestInterceptor;

import com.malgn.auth.client.AuthClient;
import com.malgn.auth.context.AuthContext;
import com.malgn.auth.context.AuthContextHolder;

@RequiredArgsConstructor
@Configuration
public class FeignConfiguration {

    private final AuthClient client;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofSeconds(10))
            .build();
    }

    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return requestTemplate -> requestTemplate.headers(getRequestHeaders());
    }

    private Map<String, Collection<String>> getRequestHeaders() {

        Map<String, Collection<String>> headers = Maps.newHashMap();

        AuthContext context = AuthContextHolder.getCurrentContext();

        if (context == null || context.getPrincipal().isExpired()) {
            client.token();
        }

        headers.put(HttpHeaders.AUTHORIZATION,
            Collections.singletonList("Bearer " + context.getPrincipal().getAccessToken()));
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        headers.put(HttpHeaders.ACCEPT, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));

        return headers;
    }
}
