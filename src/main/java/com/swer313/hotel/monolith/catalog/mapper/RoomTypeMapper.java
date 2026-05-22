package com.swer313.hotel.monolith.catalog.mapper;

import com.swer313.hotel.monolith.catalog.dto.RoomTypeCreateUpdateRequest;
import com.swer313.hotel.monolith.catalog.dto.RoomTypeDto;
import com.swer313.hotel.monolith.catalog.model.Hotel;
import com.swer313.hotel.monolith.catalog.model.RoomType;

/**
 * Mapper for converting between RoomType entities and DTOs.
 */
public class RoomTypeMapper {

    private RoomTypeMapper() {
    }

    public static RoomTypeDto toDto(RoomType entity) {
        RoomTypeDto dto = new RoomTypeDto();
        dto.setId(entity.getId());
        dto.setHotelId(entity.getHotel().getId());
        dto.setName(entity.getName());
        dto.setCapacity(entity.getCapacity());
        dto.setTotalRooms(entity.getTotalRooms());
        dto.setBasePricePerNight(entity.getBasePricePerNight());
        dto.setAmenities(entity.getAmenities());
        return dto;
    }

    public static RoomType toEntity(RoomTypeCreateUpdateRequest request, Hotel hotel) {
        RoomType entity = new RoomType();
        entity.setHotel(hotel);
        updateEntityFromRequest(request, entity);
        return entity;
    }

    public static void updateEntityFromRequest(RoomTypeCreateUpdateRequest request, RoomType entity) {
        entity.setName(request.getName());
        entity.setCapacity(request.getCapacity());
        entity.setTotalRooms(request.getTotalRooms());
        entity.setBasePricePerNight(request.getBasePricePerNight());
        entity.setAmenities(request.getAmenities());
    }
}

