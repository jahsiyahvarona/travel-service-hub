package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    // Additional query methods if needed
}
