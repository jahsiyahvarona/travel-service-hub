package com.group5.travel_service_hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/CostumerDashboard")
    public String showDashboard() {
        return "frontendCode/CustomerUI/customerDashboard"; // This is assuming you're using Thymeleaf
    }

    @GetMapping("/ProviderDashboard")
    public String showProviderDashboard() {
        return "frontendCode/ProviderUI/dashboard"; // This is assuming you're using Thymeleaf
    }

    @GetMapping("/SysAdminDashboard")
    public String showSysAdminDashboard() {
        return "frontendCode/SysAdminUI/admin_dashboard"; // This is assuming you're using Thymeleaf
    }


}
