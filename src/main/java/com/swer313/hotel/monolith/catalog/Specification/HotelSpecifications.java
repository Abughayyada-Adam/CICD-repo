package com.swer313.hotel.monolith.catalog.Specification;

import com.swer313.hotel.monolith.catalog.model.Hotel;
import com.swer313.hotel.monolith.catalog.model.RoomType;
import jakarta.persistence.criteria.Join;

import jakarta.persistence.criteria.JoinType;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HotelSpecifications {

    public static Specification<Hotel> byCity(String city) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(city)) {
                return cb.conjunction(); // always true
            }
            return cb.like(
                    cb.lower(root.get("city")),
                    "%" + city.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Hotel> byName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Hotel> byStarRatingMin(Integer min) {
        return (root, query, cb) -> {
            if (min == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("starRating"), min);
        };
    }

    public static Specification<Hotel> byStarRatingMax(Integer max) {
        return (root, query, cb) -> {
            if (max == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("starRating"), max);
        };
    }

    public static Specification<Hotel> byAmenities(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return cb.conjunction();
            }
            // Example: amenities column stores "WiFi,Pool,Parking"
            // We check if any of the requested amenities is contained in the column.
            // This is not efficient for large datasets, but works for demo.
            List<Predicate> amenityPredicates = new ArrayList<>();
            for (String amenity : amenities) {
                amenityPredicates.add(cb.like(root.get("amenities"), "%" + amenity + "%"));
            }
            return cb.or(amenityPredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Hotel> byPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return cb.conjunction();
            }
            Join<Hotel, RoomType> roomTypes = root.join("roomTypes", JoinType.INNER);
            List<Predicate> pricePredicates = new ArrayList<>();
            if (minPrice != null) {
                pricePredicates.add(cb.greaterThanOrEqualTo(roomTypes.get("basePricePerNight"), minPrice));
            }
            if (maxPrice != null) {
                pricePredicates.add(cb.lessThanOrEqualTo(roomTypes.get("basePricePerNight"), maxPrice));
            }
            // To avoid duplicate hotels when multiple room types match, we use distinct
            query.distinct(true);
            return cb.and(pricePredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Hotel> byPriceRangeAllRooms(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return cb.conjunction();
            }
            Join<Hotel, RoomType> roomTypes = root.join("roomTypes", JoinType.INNER);
            List<Predicate> pricePredicates = new ArrayList<>();
            if (minPrice != null) {
                pricePredicates.add(cb.greaterThanOrEqualTo(roomTypes.get("basePricePerNight"), minPrice));
            }
            if (maxPrice != null) {
                pricePredicates.add(cb.lessThanOrEqualTo(roomTypes.get("basePricePerNight"), maxPrice));
            }
            // For "all rooms", we need to check that no room type violates the range.
            // This is more complex; usually we want "any room type".
            // So the first method is more common.
            return cb.and(pricePredicates.toArray(new Predicate[0]));
        };
    }
}