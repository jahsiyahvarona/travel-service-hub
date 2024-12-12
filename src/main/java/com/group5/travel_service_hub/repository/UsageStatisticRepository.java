package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.UsageStatistic;
import com.group5.travel_service_hub.entity.UsageStatistic.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsageStatisticRepository extends JpaRepository<UsageStatistic, Long> {

    /**
     * Find all usage statistics by a specific metric type (e.g., ACTIVE_USERS, BOOKINGS).
     *
     * @param metricType The type of metric to filter by.
     * @return List of UsageStatistic entities matching the metric type.
     */
    List<UsageStatistic> findByMetricType(MetricType metricType);

    /**
     * Find usage statistics for a specific metric type within a date range.
     *
     * @param metricType The type of metric to filter by.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @return List of UsageStatistic entities matching the metric type and date range.
     */
    List<UsageStatistic> findByMetricTypeAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
            MetricType metricType, LocalDate startDate, LocalDate endDate);

    /**
     * Retrieve statistics by date range, regardless of metric type.
     *
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @return List of UsageStatistic entities within the specified date range.
     */
    List<UsageStatistic> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(
            LocalDate startDate, LocalDate endDate);

    /**
     * Retrieve the latest usage statistic for a specific metric type.
     *
     * @param metricType The type of metric.
     * @return The latest UsageStatistic for the specified metric type, wrapped in an Optional.
     */
    Optional<UsageStatistic> findTopByMetricTypeOrderByEndDateDesc(MetricType metricType);

    /**
     * Retrieve usage statistics by metric type with a value greater than a specified threshold.
     *
     * @param metricType The type of metric.
     * @param threshold  The minimum value threshold.
     * @return List of UsageStatistic entities with values above the threshold for the specified metric.
     */
    List<UsageStatistic> findByMetricTypeAndValueGreaterThan(MetricType metricType, BigDecimal threshold);

    /**
     * Calculate the average value for a specific metric type within a date range.
     * Useful for sysadmins to get an overview of metrics like average user activity.
     *
     * @param metricType The type of metric.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @return The average value as a BigDecimal, wrapped in an Optional.
     */
    @Query("SELECT AVG(u.value) FROM UsageStatistic u WHERE u.metricType = :metricType " +
            "AND u.startDate >= :startDate AND u.endDate <= :endDate")
    Optional<BigDecimal> findAverageValueByMetricTypeAndDateRange(
            @Param("metricType") MetricType metricType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Retrieve the maximum value recorded for a specific metric type within a date range.
     * Useful for identifying peak metrics like maximum active users.
     *
     * @param metricType The type of metric.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @return The maximum value as a BigDecimal, wrapped in an Optional.
     */
    @Query("SELECT MAX(u.value) FROM UsageStatistic u WHERE u.metricType = :metricType " +
            "AND u.startDate >= :startDate AND u.endDate <= :endDate")
    Optional<BigDecimal> findMaxValueByMetricTypeAndDateRange(
            @Param("metricType") MetricType metricType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Retrieve the minimum value recorded for a specific metric type within a date range.
     *
     * @param metricType The type of metric.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @return The minimum value as a BigDecimal, wrapped in an Optional.
     */
    @Query("SELECT MIN(u.value) FROM UsageStatistic u WHERE u.metricType = :metricType " +
            "AND u.startDate >= :startDate AND u.endDate <= :endDate")
    Optional<BigDecimal> findMinValueByMetricTypeAndDateRange(
            @Param("metricType") MetricType metricType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
