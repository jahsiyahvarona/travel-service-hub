package com.group5.travel_service_hub.config;

import com.group5.travel_service_hub.entity.City;
import com.group5.travel_service_hub.repository.CityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

/**
 * Service to initialize City data in the database.
 */
@Component
public class CityDataInitializer implements CommandLineRunner {

    private final CityRepository cityRepository;

    public CityDataInitializer(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if cities are already populated
        if (cityRepository.count() > 0) {
            System.out.println("Cities are already populated.");
            return;
        }

        List<City> cities = Arrays.asList(
                new City(1L, "New York", "New York", "United States"),
                new City(2L, "Los Angeles", "California", "United States"),
                new City(3L, "Chicago", "Illinois", "United States"),
                new City(4L, "Houston", "Texas", "United States"),
                new City(5L, "Phoenix", "Arizona", "United States"),
                new City(6L, "San Francisco", "California", "United States"),
                new City(7L, "Miami", "Florida", "United States"),
                new City(8L, "Dallas", "Texas", "United States"),
                new City(9L, "Atlanta", "Georgia", "United States"),
                new City(10L, "Denver", "Colorado", "United States"),
                new City(11L, "London", null, "United Kingdom"),
                new City(12L, "Tokyo", null, "Japan"),
                new City(13L, "Paris", null, "France"),
                new City(14L, "Berlin", null, "Germany"),
                new City(15L, "Sydney", null, "Australia"),
                new City(16L, "Melbourne", null, "Australia"),
                new City(17L, "Toronto", "Ontario", "Canada"),
                new City(18L, "Vancouver", "British Columbia", "Canada"),
                new City(19L, "Montreal", "Quebec", "Canada"),
                new City(20L, "Dubai", null, "United Arab Emirates"),
                new City(21L, "Singapore", null, "Singapore"),
                new City(22L, "Mumbai", "Maharashtra", "India"),
                new City(23L, "Delhi", null, "India"),
                new City(24L, "Bangalore", "Karnataka", "India"),
                new City(25L, "Cape Town", null, "South Africa"),
                new City(26L, "Johannesburg", null, "South Africa"),
                new City(27L, "Rio de Janeiro", null, "Brazil"),
                new City(28L, "São Paulo", null, "Brazil"),
                new City(29L, "Buenos Aires", null, "Argentina"),
                new City(30L, "Mexico City", null, "Mexico"),
                new City(31L, "Guadalajara", null, "Mexico"),
                new City(32L, "Santiago", null, "Chile"),
                new City(33L, "Bogotá", null, "Colombia"),
                new City(34L, "Lima", null, "Peru"),
                new City(35L, "Caracas", null, "Venezuela"),
                new City(36L, "Madrid", null, "Spain"),
                new City(37L, "Barcelona", null, "Spain"),
                new City(38L, "Rome", null, "Italy"),
                new City(39L, "Milan", null, "Italy"),
                new City(40L, "Naples", null, "Italy"),
                new City(41L, "Istanbul", null, "Turkey"),
                new City(42L, "Ankara", null, "Turkey"),
                new City(43L, "Athens", null, "Greece"),
                new City(44L, "Cairo", null, "Egypt"),
                new City(45L, "Lagos", null, "Nigeria"),
                new City(46L, "Abuja", null, "Nigeria"),
                new City(47L, "Accra", null, "Ghana"),
                new City(48L, "Casablanca", null, "Morocco"),
                new City(49L, "Marrakesh", null, "Morocco"),
                new City(50L, "Nairobi", null, "Kenya"),
                new City(51L, "Johor Bahru", null, "Malaysia"),
                new City(52L, "Bangkok", null, "Thailand"),
                new City(53L, "Phuket", null, "Thailand"),
                new City(54L, "Ho Chi Minh City", null, "Vietnam"),
                new City(55L, "Hanoi", null, "Vietnam"),
                new City(56L, "Jakarta", null, "Indonesia"),
                new City(57L, "Surabaya", null, "Indonesia"),
                new City(58L, "Kuala Lumpur", null, "Malaysia"),
                new City(59L, "Manila", null, "Philippines"),
                new City(60L, "Cebu", null, "Philippines")
                // Add cities up to 500
        );


        // Save all cities to the database
        cityRepository.saveAll(cities);
        System.out.println("Cities have been successfully populated.");
    }
}
