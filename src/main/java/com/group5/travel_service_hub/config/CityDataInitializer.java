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
                new City(60L, "Cebu", null, "Philippines"),
                new City(61L, "Birmingham", "Alabama", "United States"),
                new City(62L, "Anchorage", "Alaska", "United States"),
                new City(63L, "Phoenix", "Arizona", "United States"),
                new City(64L, "Little Rock", "Arkansas", "United States"),
                new City(65L, "Los Angeles", "California", "United States"),
                new City(66L, "Denver", "Colorado", "United States"),
                new City(67L, "Bridgeport", "Connecticut", "United States"),
                new City(68L, "Wilmington", "Delaware", "United States"),
                new City(69L, "Jacksonville", "Florida", "United States"),
                new City(70L, "Atlanta", "Georgia", "United States"),
                new City(71L, "Honolulu", "Hawaii", "United States"),
                new City(72L, "Boise", "Idaho", "United States"),
                new City(73L, "Chicago", "Illinois", "United States"),
                new City(74L, "Indianapolis", "Indiana", "United States"),
                new City(75L, "Des Moines", "Iowa", "United States"),
                new City(76L, "Wichita", "Kansas", "United States"),
                new City(77L, "Louisville", "Kentucky", "United States"),
                new City(78L, "New Orleans", "Louisiana", "United States"),
                new City(79L, "Portland", "Maine", "United States"),
                new City(80L, "Baltimore", "Maryland", "United States"),
                new City(81L, "Boston", "Massachusetts", "United States"),
                new City(82L, "Detroit", "Michigan", "United States"),
                new City(83L, "Minneapolis", "Minnesota", "United States"),
                new City(84L, "Jackson", "Mississippi", "United States"),
                new City(85L, "Kansas City", "Missouri", "United States"),
                new City(86L, "Billings", "Montana", "United States"),
                new City(87L, "Omaha", "Nebraska", "United States"),
                new City(88L, "Las Vegas", "Nevada", "United States"),
                new City(89L, "Manchester", "New Hampshire", "United States"),
                new City(90L, "Newark", "New Jersey", "United States"),
                new City(91L, "Albuquerque", "New Mexico", "United States"),
                new City(92L, "New York City", "New York", "United States"),
                new City(93L, "Charlotte", "North Carolina", "United States"),
                new City(94L, "Fargo", "North Dakota", "United States"),
                new City(95L, "Columbus", "Ohio", "United States"),
                new City(96L, "Oklahoma City", "Oklahoma", "United States"),
                new City(97L, "Portland", "Oregon", "United States"),
                new City(98L, "Philadelphia", "Pennsylvania", "United States"),
                new City(99L, "Providence", "Rhode Island", "United States"),
                new City(100L, "Columbia", "South Carolina", "United States"),
                new City(101L, "Sioux Falls", "South Dakota", "United States"),
                new City(102L, "Memphis", "Tennessee", "United States"),
                new City(103L, "Houston", "Texas", "United States"),
                new City(104L, "Salt Lake City", "Utah", "United States"),
                new City(105L, "Burlington", "Vermont", "United States"),
                new City(106L, "Virginia Beach", "Virginia", "United States"),
                new City(107L, "Seattle", "Washington", "United States"),
                new City(108L, "Charleston", "West Virginia", "United States"),
                new City(109L, "Milwaukee", "Wisconsin", "United States"),
                new City(110L, "Cheyenne", "Wyoming", "United States"),
                new City(111L, "Malé", null, "Maldives"),
                new City(112L, "Santorini", null, "Greece"),
                new City(113L, "Bora Bora", null, "French Polynesia"),
                new City(114L, "Machu Picchu", null, "Peru"),
                new City(115L, "Queenstown", null, "New Zealand"),
                new City(116L, "Banff", "Alberta", "Canada"),
                new City(117L, "Reykjavik", null, "Iceland"),
                new City(118L, "Florence", null, "Italy"),
                new City(119L, "Venice", null, "Italy"),
                new City(120L, "Edinburgh", null, "Scotland"),
                new City(121L, "Havana", null, "Cuba"),
                new City(122L, "Antigua", null, "Guatemala"),
                new City(123L, "Dubrovnik", null, "Croatia"),
                new City(124L, "Koh Samui", null, "Thailand"),
                new City(125L, "Kyoto", null, "Japan"),
                new City(126L, "Fiji", null, "Fiji"),
                new City(127L, "Monte Carlo", null, "Monaco"),
                new City(128L, "Zanzibar", null, "Tanzania"),
                new City(129L, "Petra", null, "Jordan"),
                new City(130L, "Jerusalem", null, "Israel"),
                new City(131L, "Hong Kong", null, "China"),
                new City(132L, "Seoul", null, "South Korea"),
                new City(133L, "Marrakech", null, "Morocco"),
                new City(134L, "Siem Reap", null, "Cambodia"),
                new City(135L, "Tahiti", null, "French Polynesia"),
                new City(136L, "Cape Town", null, "South Africa"),
                new City(137L, "Cusco", null, "Peru"),
                new City(138L, "Yellowstone National Park", null, "United States"),
                new City(139L, "Whistler", "British Columbia", "Canada"),
                new City(140L, "Aspen", "Colorado", "United States"),
                new City(141L, "Orlando", "Florida", "United States"),
                new City(142L, "Cancún", null, "Mexico"),
                new City(143L, "Tulum", null, "Mexico"),
                new City(144L, "Hvar", null, "Croatia")
        );


        // Save all cities to the database
        cityRepository.saveAll(cities);
        System.out.println("Cities have been successfully populated.");
    }
}
