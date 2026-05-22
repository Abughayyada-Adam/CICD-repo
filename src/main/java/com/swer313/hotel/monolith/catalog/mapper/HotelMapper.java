package com.swer313.hotel.monolith.catalog.mapper;

import com.swer313.hotel.monolith.catalog.dto.HotelCreateUpdateRequest;
import com.swer313.hotel.monolith.catalog.dto.HotelDto;
import com.swer313.hotel.monolith.catalog.dto.RoomTypeSummaryDto;
import com.swer313.hotel.monolith.catalog.model.Hotel;
import com.swer313.hotel.monolith.catalog.model.RoomType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for translating between Hotel entities and DTOs.
 * Keeping this mapping logic in a dedicated class makes controllers and services cleaner.
 */
public class HotelMapper {

    private HotelMapper() {
    }

    public static HotelDto toDto(Hotel entity) {
        HotelDto dto = new HotelDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCity(entity.getCity());
        dto.setAddress(entity.getAddress());
        dto.setStarRating(entity.getStarRating());

        // Convert lazy‑loaded room types into lightweight summaries if they are already initialized.
        List<RoomType> roomTypes = entity.getRoomTypes();
        if (roomTypes != null) {
            List<RoomTypeSummaryDto> summaries = roomTypes.stream()
                    .map(HotelMapper::toRoomTypeSummary)
                    .collect(Collectors.toList());
            dto.setRoomTypes(summaries);
        }

        return dto;
    }

    public static RoomTypeSummaryDto toRoomTypeSummary(RoomType roomType) {
        RoomTypeSummaryDto dto = new RoomTypeSummaryDto();
        dto.setId(roomType.getId());
        dto.setName(roomType.getName());
        dto.setCapacity(roomType.getCapacity());
        dto.setBasePricePerNight(roomType.getBasePricePerNight());
        return dto;
    }

    public static void updateEntityFromRequest(HotelCreateUpdateRequest request, Hotel entity) {
        // We treat the request as the single source of truth for mutable fields.
        entity.setName(request.getName());
        entity.setCity(request.getCity());
        entity.setAddress(request.getAddress());
        entity.setStarRating(request.getStarRating());
    }
}

