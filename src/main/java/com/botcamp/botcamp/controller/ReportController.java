package com.botcamp.botcamp.controller;

import com.botcamp.botcamp.service.reporting.Report;
import org.apache.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public interface ReportController {
    ResponseEntity<Report> generateReport(String beginDate, String endDate);

    String GENERATE_REPORT_ENDPOINT = "/generate";
}
