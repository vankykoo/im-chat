package com.vanky.chat.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author vanky
 * @create 2024/5/18 22:25
 */
@Controller
public class PageController {

    @GetMapping("/auth/oauth/to_login")
    public String loginPath(){
        return "mylogin";
    }
}
