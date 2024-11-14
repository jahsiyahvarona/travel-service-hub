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

@Controller
public class DashboardController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/CostumerDashboard")
    public String showDashboard() {
        return "frontendCode/CustomerUI/customerDashboard"; // This is assuming you're using Thymeleaf
    }

    @GetMapping("/ProviderDashboard")
    public String showProviderDashboard(HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/ProviderLogin";  // Redirect to login if user is not found in the session
        }

        User provider = userService.findByUsername(loggedInUser.getUsername());

        // Fetch all bookings specific to the provider's packages
        List<Booking> allBookings = bookingRepository.findAllDistinctBookingsByProviderDetailsId(provider.getId());

        // Filter confirmed bookings for sales calculations
        List<Booking> confirmedBookings = allBookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CONFIRMED)
                .toList();

        // Aggregate bookings data by month for the booking performance chart (All bookings)
        Map<String, Long> bookingsCountByMonth = allBookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTimestamp().getMonth().toString(),
                        Collectors.counting()
                ));

        // Aggregate sales data by month for the sales performance chart (Only confirmed bookings)
        Map<String, Double> salesAmountByMonth = confirmedBookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTimestamp().getMonth().toString(),
                        Collectors.summingDouble(Booking::getPrice)
                ));

        // Prepare lists for chart data
        Set<String> allMonthsSet = new HashSet<>();
        allMonthsSet.addAll(bookingsCountByMonth.keySet());
        allMonthsSet.addAll(salesAmountByMonth.keySet());
        List<String> months = new ArrayList<>(allMonthsSet);
        // Sort months in chronological order
        months.sort(Comparator.comparingInt(month -> Month.valueOf(month.toUpperCase()).getValue()));

        List<Long> bookingData = months.stream()
                .map(month -> bookingsCountByMonth.getOrDefault(month, 0L))
                .collect(Collectors.toList());

        List<Double> salesData = months.stream()
                .map(month -> salesAmountByMonth.getOrDefault(month, 0.0))
                .collect(Collectors.toList());

        // Calculate total sales from confirmed bookings
        double totalSales = confirmedBookings.stream()
                .mapToDouble(Booking::getPrice)
                .sum();

        // Find the most liked package
        List<Package> providerPackages = packageRepository.findByProviderDetailsId(provider.getId());
        Package mostLikedPackage = providerPackages.stream()
                .max(Comparator.comparingInt(pkg -> (int) pkg.getLikeDislikes().stream().filter(LikeDislike::isLike).count()))
                .orElse(null);

        long likesCount = 0;
        long bookingsCountForMostLiked = 0;
        String packageStatusForMostLiked = "Active"; // Default status

        if (mostLikedPackage != null) {
            likesCount = mostLikedPackage.getLikeDislikes().stream()
                    .filter(LikeDislike::isLike)
                    .count();
            bookingsCountForMostLiked = bookingRepository.countByPkg(mostLikedPackage);
            // If Package has a 'status' field, uncomment the line below
            // packageStatusForMostLiked = mostLikedPackage.getStatus();
        }

        // Find the most booked package
        Map<Package, Long> bookingsCountByPackage = confirmedBookings.stream()
                .collect(Collectors.groupingBy(Booking::getPkg, Collectors.counting()));

        Package mostBookedPackage = bookingsCountByPackage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        long bookingsCountForMostBooked = 0;
        long likesCountForMostBooked = 0;
        String packageStatusForMostBooked = "Active"; // Default status

        if (mostBookedPackage != null) {
            bookingsCountForMostBooked = bookingsCountByPackage.get(mostBookedPackage);
            likesCountForMostBooked = mostBookedPackage.getLikeDislikes().stream()
                    .filter(LikeDislike::isLike)
                    .count();
            // If Package has a 'status' field, uncomment the line below
            // packageStatusForMostBooked = mostBookedPackage.getStatus();
        }

        // Add data to the model
        model.addAttribute("profilePicUrl",provider.getProfilePic());
        model.addAttribute("providerName", provider.getUsername());
        model.addAttribute("months", months);
        model.addAttribute("bookingData", bookingData);
        model.addAttribute("salesData", salesData);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("mostLikedPackage", mostLikedPackage);
        model.addAttribute("likesCount", likesCount);
        model.addAttribute("bookingsCountForMostLiked", bookingsCountForMostLiked);
        model.addAttribute("packageStatusForMostLiked", packageStatusForMostLiked);
        model.addAttribute("mostBookedPackage", mostBookedPackage);
        model.addAttribute("bookingsCountForMostBooked", bookingsCountForMostBooked);
        model.addAttribute("likesCountForMostBooked", likesCountForMostBooked);
        model.addAttribute("packageStatusForMostBooked", packageStatusForMostBooked);

        return "frontendCode/ProviderUI/dashboard"; // Replace with your actual dashboard view name
    }


    @GetMapping("/SysAdminDashboard")
    public String showSysAdminDashboard() {
        return "frontendCode/SysAdminUI/admin_dashboard"; // This is assuming you're using Thymeleaf
    }

}




