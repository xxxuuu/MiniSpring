package com.xqinger.service;

import com.xqinger.beans.Bean;

/**
 * @author XUQING
 * @date 2019-07-28
 */
@Bean
public class TestService {
    public String test(String world) {
        return "hello " + world;
    }
}
