package com.swer313.hotel.monolith.catalog.dto;

import java.util.List;

/**
 * HotelDto is the shape of hotel data that we expose over the API layer.
 * It intentionally hides JPA details (lazy loading, entity graphs, etc.).
 */
public class HotelDto {

    private Long id;
    private String name;
    private String city;
    private String address;
    private Integer starRating;
    private List<RoomTypeSummaryDto> roomTypes;

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

    public List<RoomTypeSummaryDto> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomTypeSummaryDto> roomTypes) {
        this.roomTypes = roomTypes;
    }
}

