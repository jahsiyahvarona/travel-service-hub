package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.repository.BookingRepository;
import com.group5.travel_service_hub.repository.PackageRepository;
import com.group5.travel_service_hub.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for handling dashboard-related operations for customers, providers, and system administrators.
 */
@Controller
public class DashboardController {

    @Autowired
    private BookingRepository bookingRepository; // Repository for booking-related data

    @Autowired
    private PackageRepository packageRepository; // Repository for package-related data

    @Autowired
    private UserService userService; // Service for managing user-related operations

    /**
     * Displays the customer dashboard with aggregated booking and spending data.
     *
     * @param session The current HTTP session to retrieve the logged-in user.
     * @param model   The model to pass data to the view.
     * @return The customer dashboard view.
     */
    @GetMapping("/CostumerDashboard")
    public String showCustomerDashboard(HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/LoginChoice"; // Redirect to login if user is not logged in
        }

        User customer = userService.findByUsername(loggedInUser.getUsername());

        // Fetch all bookings for the logged-in customer
        List<Booking> customerBookings = bookingRepository.findByCustomerId(customer.getId());

        // Calculate the total spending of the customer
        double totalSpending = customerBookings.stream()
                .mapToDouble(booking -> booking.getPkg().getPrice())
                .sum();

        // Count the total number of bookings
        long totalBookings = customerBookings.size();

        // Determine the most booked package for the customer
        Map<Package, Long> bookingsCountByPackage = customerBookings.stream()
                .collect(Collectors.groupingBy(Booking::getPkg, Collectors.counting()));

        Package favoritePackage = bookingsCountByPackage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Pass the data to the model
        model.addAttribute("userName", customer.getUsername());
        model.addAttribute("totalSpending", totalSpending);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("favoritePackage", favoritePackage != null ? favoritePackage.getName() : "N/A");
        model.addAttribute("profilePicUrl", customer.getProfilePic());

        return "frontendCode/CustomerUI/customerDashboard"; // View for the customer dashboard
    }

    /**
     * Displays the provider dashboard with booking and sales performance metrics.
     *
     * @param session The current HTTP session to retrieve the logged-in user.
     * @param model   The model to pass data to the view.
     * @return The provider dashboard view.
     */
    @GetMapping("/ProviderDashboard")
    public String showProviderDashboard(HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/ProviderLogin"; // Redirect to login if user is not logged in
        }

        User provider = userService.findByUsername(loggedInUser.getUsername());

        // Fetch all bookings for the provider's packages
        List<Booking> bookings = bookingRepository.findByProvider(provider);

        // Filter only confirmed bookings
        List<Booking> confirmedBookings = bookings.stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.CONFIRMED))
                .toList();

        // Aggregate booking data by month for the performance chart
        Map<String, Long> bookingsCountByMonth = confirmedBookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTimestamp().getMonth().toString(),
                        Collectors.counting()
                ));

        // Aggregate sales data by month for the performance chart
        Map<String, Double> salesAmountByMonth = confirmedBookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTimestamp().getMonth().toString(),
                        Collectors.summingDouble(Booking::getPrice)
                ));

        // Prepare chart data
        List<String> months = new ArrayList<>(bookingsCountByMonth.keySet());
        months.sort(Comparator.comparingInt(month -> Month.valueOf(month.toUpperCase()).getValue()));

        List<Long> bookingData = months.stream()
                .map(bookingsCountByMonth::get)
                .collect(Collectors.toList());

        List<Double> salesData = months.stream()
                .map(salesAmountByMonth::get)
                .collect(Collectors.toList());

        // Calculate total sales
        double totalSales = confirmedBookings.stream()
                .mapToDouble(Booking::getPrice)
                .sum();

        // Find the most liked package
        List<Package> providerPackages = packageRepository.findByProviderDetailsId(provider.getId());
        Package mostLikedPackage = providerPackages.stream()
                .max(Comparator.comparingInt(pkg -> (int) pkg.getLikeDislikes().stream().filter(LikeDislike::isLike).count()))
                .orElse(null);

        long likesCount = mostLikedPackage != null
                ? mostLikedPackage.getLikeDislikes().stream().filter(LikeDislike::isLike).count()
                : 0;

        // Find the most booked package
        Map<Package, Long> bookingsCountByPackage = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getPkg, Collectors.counting()));

        Package mostBookedPackage = bookingsCountByPackage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        long bookingsCountForMostBooked = mostBookedPackage != null ? bookingsCountByPackage.get(mostBookedPackage) : 0;

        // Pass the data to the model
        model.addAttribute("profilePicUrl", provider.getProfilePic());
        model.addAttribute("providerName", provider.getUsername());
        model.addAttribute("months", months);
        model.addAttribute("bookingData", bookingData);
        model.addAttribute("salesData", salesData);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("mostLikedPackage", mostLikedPackage);
        model.addAttribute("likesCount", likesCount);
        model.addAttribute("mostBookedPackage", mostBookedPackage);
        model.addAttribute("bookingsCountForMostBooked", bookingsCountForMostBooked);

        return "frontendCode/ProviderUI/dashboard"; // View for the provider dashboard
    }

    /**
     * Displays the provider dashboard with booking and sales performance metrics.
     *
     * @param session The current HTTP session to retrieve the logged-in user.
     * @param model   The model to pass data to the view.
     * @return The provider dashboard view.
     */
    @GetMapping("/newProvider")
    public String showNewProviderDashboard(HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/ProviderLogin"; // Redirect to login if user is not logged in
        }

        User provider = userService.findByUsername(loggedInUser.getUsername());

        // Fetch all bookings for the provider's packages
        List<Booking> bookings = bookingRepository.findByProvider(provider);

        // Filter only confirmed bookings
        List<Booking> confirmedBookings = bookings.stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.CONFIRMED))
                .toList();

        // Aggregate booking data by month for the performance chart
        Map<String, Long> bookingsCountByMonth = confirmedBookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTimestamp().getMonth().toString(),
                        Collectors.counting()
                ));

        // Aggregate sales data by month for the performance chart
        Map<String, Double> salesAmountByMonth = confirmedBookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTimestamp().getMonth().toString(),
                        Collectors.summingDouble(Booking::getPrice)
                ));

        // Prepare chart data
        List<String> months = new ArrayList<>(bookingsCountByMonth.keySet());
        months.sort(Comparator.comparingInt(month -> Month.valueOf(month.toUpperCase()).getValue()));

        List<Long> bookingData = months.stream()
                .map(bookingsCountByMonth::get)
                .collect(Collectors.toList());

        List<Double> salesData = months.stream()
                .map(salesAmountByMonth::get)
                .collect(Collectors.toList());

        // Calculate total sales
        double totalSales = confirmedBookings.stream()
                .mapToDouble(Booking::getPrice)
                .sum();

        // Find the most liked package
        List<Package> providerPackages = packageRepository.findByProviderDetailsId(provider.getId());
        Package mostLikedPackage = providerPackages.stream()
                .max(Comparator.comparingInt(pkg -> (int) pkg.getLikeDislikes().stream().filter(LikeDislike::isLike).count()))
                .orElse(null);

        long likesCount = mostLikedPackage != null
                ? mostLikedPackage.getLikeDislikes().stream().filter(LikeDislike::isLike).count()
                : 0;

        // Find the most booked package
        Map<Package, Long> bookingsCountByPackage = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getPkg, Collectors.counting()));

        Package mostBookedPackage = bookingsCountByPackage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        long bookingsCountForMostBooked = mostBookedPackage != null ? bookingsCountByPackage.get(mostBookedPackage) : 0;

        // Pass the data to the model
        model.addAttribute("profilePicUrl", provider.getProfilePic());
        model.addAttribute("providerName", provider.getUsername());
        model.addAttribute("months", months);
        model.addAttribute("bookingData", bookingData);
        model.addAttribute("salesData", salesData);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("mostLikedPackage", mostLikedPackage);
        model.addAttribute("likesCount", likesCount);
        model.addAttribute("mostBookedPackage", mostBookedPackage);
        model.addAttribute("bookingsCountForMostBooked", bookingsCountForMostBooked);


        return "frontendCode/ProviderUI/newProvider"; // View for the provider dashboard
    }

    /**
     * Displays the system administrator dashboard.
     *
     * @return The SysAdmin dashboard view.
     */
    @GetMapping("/SysAdminDashboard")
    public String showSysAdminDashboard() {
        return "frontendCode/SysAdminUI/admin_dashboard"; // View for the SysAdmin dashboard
    }

}




