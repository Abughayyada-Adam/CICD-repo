package com.swer313.hotel.monolith.catalog.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Hotel is a root entity in the catalog module.
 * It represents a hotel that can contain multiple room types.
 */
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Human readable hotel name that will appear in search results.
     */
    @Column(nullable = false)
    private String name;

    /**
     * City or region where the hotel is located.
     * In a real system this could be normalized into a separate table.
     */
    @Column(nullable = false)
    private String city;

    /**
     * Free form address string for display purposes.
     */
    @Column(nullable = false)
    private String address;

    /**
     * Simple rating representation (e.g. 3, 4, 5 stars).
     */
    @Column(nullable = false)
    private Integer starRating;
    // In Hotel.java
    @Column(length = 500)
    private String amenities;  // e.g., "WiFi,Pool,Parking"
    /**
     * Bi-directional link to the room types that belong to this hotel.
     * We use LAZY fetching so that loading a hotel does not immediately pull all room types.
     */
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoomType> roomTypes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStarRating() {
        return starRating;
    }

    public void setStarRating(Integer starRating) {
        this.starRating = starRating;
    }

    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }
}

