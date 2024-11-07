package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.UsageStatistic;
import com.group5.travel_service_hub.repository.UsageStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsageStatisticService {

    private final UsageStatisticRepository usageStatisticRepository;

    @Autowired
    public UsageStatisticService(UsageStatisticRepository usageStatisticRepository) {
        this.usageStatisticRepository = usageStatisticRepository;
    }

    public UsageStatistic saveUsageStatistic(UsageStatistic usageStatistic) {
        return usageStatisticRepository.save(usageStatistic);
    }

    public Optional<UsageStatistic> findById(Long id) {
        return usageStatisticRepository.findById(id);
    }

    public List<UsageStatistic> findByMetricType(UsageStatistic.MetricType metricType) {
        return usageStatisticRepository.findByMetricType(metricType);
    }

    // Other methods for filtering, averaging, etc., as defined in UsageStatisticRepository
}
