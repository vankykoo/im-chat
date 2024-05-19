package com.vanky.chat.auth;

import com.vanky.chat.auth.utils.ImJwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vanky
 * @create 2024/5/15 21:54
 */
@SpringBootTest
public class JwtTest {

    @Resource
    private ImJwtUtil jwtUtil;

    @Test
    public void test(){
        Map<String, Object> map = new HashMap<>();
        map.put("username", "vanky");

        //String token = jwtUtil.createToken(map);

        //System.out.println(token);
    }

    @Test
    public void testParseToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTU3ODE2MDcsImlhdCI6MTcxNTc4MTU5NywidXNlcm5hbWUiOiJ2YW5reSJ9.1fG5Mg68K4i7vQ5u-ucIs0U2pOjO2HjALTfpe7xXQxE";

        //Claims claims = jwtUtil.parseToken(token);

        //System.out.println(claims);
    }

}
