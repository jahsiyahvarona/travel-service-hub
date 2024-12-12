package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.Statistic;
import com.group5.travel_service_hub.repository.BookingRepository;
import com.group5.travel_service_hub.repository.PackageRepository;
import com.group5.travel_service_hub.repository.ReportRepository;
import com.group5.travel_service_hub.repository.UserRepository;

import java.util.List;

public class getAppStatistics() {
    long totalUsers = UserRepository.count();
    long totalPackages = PackageRepository.count();
    long totalBookings = BookingRepository.count();
    long totalReports = ReportRepository.count();

    // Create a Statistic object with the gathered data
    Statistic stats = new Statistic(totalUsers, totalPackages, totalBookings, totalReports);

    // Return the statistics as a list
    return List.of(stats);  // Returning a list for consistency, but you could also return just the object if needed
}
