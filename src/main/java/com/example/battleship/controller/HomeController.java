package com.example.battleship.controller;

import com.example.battleship.DatabaseLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @RequestMapping(value = "/")
    public String index() {
        logger.info("visiting home page");
        return "index";
    }
}

