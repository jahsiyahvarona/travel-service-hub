package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Booking;
import com.group5.travel_service_hub.entity.LikeDislike;
import com.group5.travel_service_hub.entity.User;
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
    public String showCustomerDashboard(HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not found in the session
        }

        User customer = userService.findByUsername(loggedInUser.getUsername());

        // Fetch all bookings for the logged-in customer
        List<Booking> customerBookings = bookingRepository.findByCustomerId(customer.getId());

        // Calculate total spending
        double totalSpending = customerBookings.stream()
                .mapToDouble(booking -> booking.getPkg().getPrice())
                .sum();

        // Calculate total bookings
        long totalBookings = customerBookings.size();

        // Find the most booked package for the customer
        Map<Package, Long> bookingsCountByPackage = customerBookings.stream()
                .collect(Collectors.groupingBy(Booking::getPkg, Collectors.counting()));

        Package favoritePackage = bookingsCountByPackage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Pass data to the model
        model.addAttribute("userName", customer.getUsername());
        model.addAttribute("totalSpending", totalSpending);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("favoritePackage", favoritePackage != null ? favoritePackage.getName() : "N/A");
        model.addAttribute("profilePicUrl", customer.getProfilePic());

        return "frontendCode/CustomerUI/customerDashboard";
    }

    @GetMapping("/ProviderDashboard")
    public String showProviderDashboard(HttpSession session, Model model) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not found in the session
        }

        User provider = userService.findByUsername(loggedInUser.getUsername());

        // Fetch bookings specific to the provider's packages
        List<Booking> bookings = bookingRepository.findByProvider(provider);

        // Aggregate bookings data by month for the booking performance chart
        Map<String, Long> bookingsCountByMonth = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTimestamp().getMonth().toString(),
                        Collectors.counting()
                ));

        // Aggregate sales data by month for the sales performance chart
        Map<String, Double> salesAmountByMonth = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTimestamp().getMonth().toString(),
                        Collectors.summingDouble(booking -> booking.getPkg().getPrice())
                ));

        // Prepare lists for chart data
        List<String> months = new ArrayList<>(bookingsCountByMonth.keySet());
        // Sort months in chronological order
        months.sort(Comparator.comparingInt(month -> Month.valueOf(month.toUpperCase()).getValue()));

        List<Long> bookingData = months.stream()
                .map(bookingsCountByMonth::get)
                .collect(Collectors.toList());

        List<Double> salesData = months.stream()
                .map(salesAmountByMonth::get)
                .collect(Collectors.toList());

        // Calculate total sales
        double totalSales = bookings.stream()
                .mapToDouble(booking -> booking.getPkg().getPrice())
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
        Map<Package, Long> bookingsCountByPackage = bookings.stream()
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
// Add profile picture URL to the model
        // Pass data to the model
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

        return "frontendCode/ProviderUI/dashboard";
    }


    @GetMapping("/SysAdminDashboard")
    public String showSysAdminDashboard() {
        return "frontendCode/SysAdminUI/admin_dashboard"; // This is assuming you're using Thymeleaf
    }

}




