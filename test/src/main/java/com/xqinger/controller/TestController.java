package com.xqinger.controller;

import com.xqinger.beans.AutoWired;
import com.xqinger.service.TestService;
import com.xqinger.web.mvc.Controller;
import com.xqinger.web.mvc.RequestMapping;
import com.xqinger.web.mvc.RequestParam;

/**
 * 测试Controller
 * @author XUQING
 * @date 2019-07-24
 */
@Controller
public class TestController {
    @AutoWired
    private TestService testService;

    @RequestMapping("/hello")
    public String hello(@RequestParam("spring") String world) {
        return testService.test(world);
    }
}
