package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.Report;
import com.group5.travel_service_hub.entity.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on the Report entity.
 * Extends JpaRepository to provide built-in methods for database interaction.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Retrieves all reports filtered by a specific status.
     *
     * @param status The status of the reports to retrieve (e.g., PENDING, APPROVED, REJECTED).
     * @return A list of Report entities matching the specified status.
     */
    List<Report> findByStatus(ReportStatus status);

    /**
     * Retrieves all reports submitted by a specific user.
     *
     * @param reporterId The ID of the user who submitted the reports.
     * @return A list of Report entities created by the specified reporter.
     */
    List<Report> findByReporterId(Long reporterId);

    /**
     * Retrieves all reports reviewed by a specific system administrator.
     *
     * @param reviewerId The ID of the system administrator who reviewed the reports.
     * @return A list of Report entities reviewed by the specified sysadmin.
     */
    List<Report> findByReviewedById(Long reviewerId);

    /**
     * Retrieves all reports related to a specific package.
     *
     * @param packageId The ID of the package associated with the reports.
     * @return A list of Report entities related to the specified package.
     */
    List<Report> findByPkgId(Long packageId);

    /**
     * Retrieves all reports associated with a specific review.
     *
     * @param reviewId The ID of the review the reports are linked to.
     * @return A list of Report entities related to the specified review.
     */
    List<Report> findByReviewId(Long reviewId);

    /**
     * Retrieves all reports related to a specific booking.
     *
     * @param bookingId The ID of the booking associated with the reports.
     * @return A list of Report entities related to the specified booking.
     */
    List<Report> findByBookingId(Long bookingId);
}
