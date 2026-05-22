package com.swer313.hotel.monolith.catalog.repository;

import com.swer313.hotel.monolith.catalog.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for CRUD operations on RoomType entities.
 */
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    /**
     * Load all room types for a specific hotel.
     */
    List<RoomType> findByHotelId(Long hotelId);
}

