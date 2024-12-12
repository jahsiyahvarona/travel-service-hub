package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.UsageStatistic;
import com.group5.travel_service_hub.service.UsageStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class UsageStatisticController {

    private final UsageStatisticService usageStatisticService;

    @Autowired
    public UsageStatisticController(UsageStatisticService usageStatisticService) {
        this.usageStatisticService = usageStatisticService;
    }

    // Endpoint to create a new statistic
    @PostMapping("/add")
    public UsageStatistic addStatistic(@RequestParam String metricType,
                                       @RequestParam BigDecimal value,
                                       @RequestParam LocalDate dateRange) {
        UsageStatistic newStatistic = new UsageStatistic();
        newStatistic.setMetricType(UsageStatistic.MetricType.valueOf(metricType));
        newStatistic.setValue(value);
        newStatistic.setDateRange(dateRange);
        return usageStatisticService.saveUsageStatistic(newStatistic);
    }

    // Endpoint to get statistics by metric type
    @GetMapping("/byType")
    public List<UsageStatistic> getByMetricType(@RequestParam String metricType) {
        return usageStatisticService.findByMetricType(UsageStatistic.MetricType.valueOf(metricType));
    }
}
