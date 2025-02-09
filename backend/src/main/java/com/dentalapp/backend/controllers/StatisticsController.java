package com.dentalapp.backend.controllers;

import com.dentalapp.backend.services.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/all")
    public Map<String, Object> getStatistics(@RequestParam LocalDate startDate) {
        LocalDate endDate = LocalDate.now();
        return statisticsService.getStatistics(startDate, endDate);
    }
}