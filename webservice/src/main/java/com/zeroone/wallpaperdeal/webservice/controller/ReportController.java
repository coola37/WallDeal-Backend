package com.zeroone.wallpaperdeal.webservice.controller;

import com.zeroone.wallpaperdeal.webservice.entity.Report;
import com.zeroone.wallpaperdeal.webservice.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @Autowired
    ReportService service;
    @PostMapping("/api/1.0/reports")
    void createReport(@RequestBody Report report){
        try {
            service.createReport(report);
        }catch (RuntimeException ex){
            throw ex;
        }
    }
}
