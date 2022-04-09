package com.baby.wind.office.assistant.controller;

import com.baby.wind.office.assistant.service.ZkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController()
@RequestMapping("/zk")
public class ZkController {

    @Resource
    private ZkService zkService;

    @GetMapping("/create")
    public void create(@RequestParam("path")String path, @RequestParam("data")String data) {
        zkService.create(path, data);
    }

    @GetMapping("/getData")
    public String getData(@RequestParam("path")String path) {
        return zkService.getData(path);
    }
}
