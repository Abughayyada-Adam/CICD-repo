package com.swer313.hotel.monolith.catalog.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * RoomType describes a type of room within a hotel
 * (e.g. Standard Double, Deluxe Suite).
 */
@Entity
@Table(name = "room_types")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Hotel that owns this room type.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    /**
     * Human‑readable room type name (e.g. "Standard Double").
     */
    @Column(nullable = false)
    private String name;

    /**
     * Maximum number of guests allowed in this room type.
     */
    @Column(nullable = false)
    private Integer capacity;

    /**
     * Total number of physical rooms of this type.
     * We use this number when preventing double‑booking.
     */
    @Column(nullable = false)
    private Integer totalRooms;

    /**
     * Base price per night before any dynamic pricing rules.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerNight;

    /**
     * Simple comma‑separated amenity list (e.g. "WiFi,Pool,Parking").
     * In a real system this would likely be a separate relation.
     */
    @Column(nullable = false, length = 500)
    private String amenities;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<com.swer313.hotel.monolith.booking.model.Booking> bookings = new java.util.ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
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

    public Integer getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(Integer totalRooms) {
        this.totalRooms = totalRooms;
    }

    public BigDecimal getBasePricePerNight() {
        return basePricePerNight;
    }

    public void setBasePricePerNight(BigDecimal basePricePerNight) {
        this.basePricePerNight = basePricePerNight;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
}

