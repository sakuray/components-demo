package com.demo.dark.management.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("process")
public class ProcessController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    @RequestMapping("view")
    public String view() {
        return "repository/processlist";
    }
}
