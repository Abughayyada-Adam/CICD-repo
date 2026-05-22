package com.swer313.hotel.monolith.catalog.dto;

import java.math.BigDecimal;

/**
 * Lightweight representation of a room type used when listing hotels.
 * We reuse a smaller shape to avoid sending unnecessary details.
 */
public class RoomTypeSummaryDto {

    private Long id;
    private String name;
    private Integer capacity;
    private BigDecimal basePricePerNight;

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getBasePricePerNight() {
        return basePricePerNight;
    }

    public void setBasePricePerNight(BigDecimal basePricePerNight) {
        this.basePricePerNight = basePricePerNight;
    }
}

