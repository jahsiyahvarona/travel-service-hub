package com.group5.travel_service_hub.entity;

public class Statistic {
    private long totalUsers;
    private long totalPackages;
    private long totalBookings;
    private long totalReports;

    // Constructor
    public Statistic(long totalUsers, long totalPackages, long totalBookings, long totalReports) {
        this.totalUsers = totalUsers;
        this.totalPackages = totalPackages;
        this.totalBookings = totalBookings;
        this.totalReports = totalReports;
    }

    // Getters and Setters
    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(long totalPackages) {
        this.totalPackages = totalPackages;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(long totalReports) {
        this.totalReports = totalReports;
    }
}
