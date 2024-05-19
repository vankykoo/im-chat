package com.vanky.chat.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign请求拦截器，为请求添加请求头
 * @author vanky
 * @create 2024/5/1 13:42
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.removeHeader("authorization");
        requestTemplate.header("authorization", "feign_inner_request_vk");
    }
}
