package com.swer313.hotel.monolith.catalog.dto;

import java.math.BigDecimal;
import java.util.List;

public class HotelFilterRequest {
    private String city;                // partial match (contains)
    private String name;                // partial match (contains)
    private Integer starRatingMin;      // minimum star rating
    private Integer starRatingMax;      // maximum star rating
    private List<String> amenities;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

// getters and setters// optional: if you later add amenities to Hotel
    // you can add more fields like: minPrice, maxPrice, etc.


    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getStarRatingMin() { return starRatingMin; }
    public void setStarRatingMin(Integer starRatingMin) { this.starRatingMin = starRatingMin; }
    public Integer getStarRatingMax() { return starRatingMax; }
    public void setStarRatingMax(Integer starRatingMax) { this.starRatingMax = starRatingMax; }
    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }


}