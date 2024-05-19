package com.vanky.chat.gateway.filter;

import com.vanky.chat.common.bo.AccessTokenInfoBO;
import com.vanky.chat.common.bo.PermissionBo;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.feign.authFeign.AuthFeignClient;
import com.vanky.chat.common.feign.userFeign.PermissionFeignClient;
import com.vanky.chat.common.response.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.http.server.reactive.TomcatHttpHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * @author vanky
 * @create 2024/5/17 15:54
 */
@Component
@Slf4j
public class ImGlobalFilter implements GlobalFilter, Ordered {

    @Value("${im.skip-paths}")
    private Set<String> skipPaths;

    @Resource
    @Lazy
    private AuthFeignClient authFeignClient;

    @Resource
    @Lazy
    private PermissionFeignClient permissionFeignClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求头
        ServerHttpRequest request = exchange.getRequest();
        if (feignRequestCheck(request)){
            return chain.filter(exchange);
        }

        //获取uri 路径
        String path = request.getURI().getPath();

        if (skipPaths.contains(path)){
            //2. 无需认证的地址
            return chain.filter(exchange);
        }

        //获取token
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null){
            //3. token 为空，需要登录
            throw new MyException.ImAuthenticationException();
        }

        //4. 验证token
        Result<AccessTokenInfoBO> result = authFeignClient.getTokenInfo(token);
        if (!result.isSuccess()){
            throw new MyException.FeignProcessException();
        }

        AccessTokenInfoBO tokenInfo = result.getData();
        //4.1 如果accessToken更新了，需要设置响应头
        if (tokenInfo.isUpdated()){
            exchange.getResponse().getHeaders().set("Authorization", tokenInfo.getToken());
        }

        //4.2 验证用户是否有访问该路径的权限
        Result<PermissionBo> res = permissionFeignClient.getPathRequestPermission(path);
        if (!res.isSuccess()){
            throw new MyException.FeignProcessException();
        }

        PermissionBo permissionBo = res.getData();
        List<String> permissions = tokenInfo.getPermissions();
        if (permissions.contains(permissionBo.getPerms())){
            //认证成功！
            return chain.filter(exchange);
        }else {
            //认证失败！
            throw new MyException.ImAuthorizationException();
        }
    }

    private boolean feignRequestCheck(ServerHttpRequest request){
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if ("feign_inner_request_vk".equals(token)){
            return true;
        }

        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
