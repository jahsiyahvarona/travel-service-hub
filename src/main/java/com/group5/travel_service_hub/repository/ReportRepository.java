package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.Report;
import com.group5.travel_service_hub.entity.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Report entity.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Finds all reports with a specific status.
     *
     * @param status The status to filter by.
     * @return List of Report entities.
     */
    List<Report> findByStatus(ReportStatus status);

    /**
     * Finds all reports submitted by a specific user.
     *
     * @param reporterId The ID of the reporter.
     * @return List of Report entities.
     */
    List<Report> findByReporterId(Long reporterId);

    /**
     * Finds all reports reviewed by a specific sysadmin.
     *
     * @param reviewerId The ID of the sysadmin.
     * @return List of Report entities.
     */
    List<Report> findByReviewedById(Long reviewerId);

    /**
     * Finds all reports associated with a specific package.
     *
     * @param packageId The ID of the package.
     * @return List of Report entities.
     */
    List<Report> findByPkgId(Long packageId);

    /**
     * Finds all reports associated with a specific review.
     *
     * @param reviewId The ID of the review.
     * @return List of Report entities.
     */
    List<Report> findByReviewId(Long reviewId);

    /**
     * Finds all reports associated with a specific booking.
     *
     * @param bookingId The ID of the booking.
     * @return List of Report entities.
     */
    List<Report> findByBookingId(Long bookingId);
}