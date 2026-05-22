package com.swer313.hotel.monolith.catalog.service;

import com.swer313.hotel.monolith.catalog.dto.RoomTypeCreateUpdateRequest;
import com.swer313.hotel.monolith.catalog.dto.RoomTypeDto;
import com.swer313.hotel.monolith.catalog.mapper.RoomTypeMapper;
import com.swer313.hotel.monolith.catalog.model.Hotel;
import com.swer313.hotel.monolith.catalog.model.RoomType;
import com.swer313.hotel.monolith.catalog.repository.RoomTypeRepository;
import com.swer313.hotel.monolith.common.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for creating and managing room types within hotels.
 */
@Service
@Transactional
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final HotelService hotelService;

    public RoomTypeService(RoomTypeRepository roomTypeRepository, HotelService hotelService) {
        this.roomTypeRepository = roomTypeRepository;
        this.hotelService = hotelService;
    }

    public RoomTypeDto createRoomType(RoomTypeCreateUpdateRequest request) {
        Hotel hotel = hotelService.getHotelEntity(request.getHotelId());
        RoomType entity = RoomTypeMapper.toEntity(request, hotel);
        RoomType saved = roomTypeRepository.save(entity);
        return RoomTypeMapper.toDto(saved);
    }

    public RoomTypeDto updateRoomType(Long id, RoomTypeCreateUpdateRequest request) {
        RoomType existing = roomTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room type with id " + id + " not found"));
        if (!existing.getHotel().getId().equals(request.getHotelId())) {
            Hotel newHotel = hotelService.getHotelEntity(request.getHotelId());
            existing.setHotel(newHotel);
        }
        RoomTypeMapper.updateEntityFromRequest(request, existing);
        return RoomTypeMapper.toDto(existing);
    }

    public void deleteRoomType(Long id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new NotFoundException("Room type with id " + id + " not found");
        }
        roomTypeRepository.deleteById(id);
    }

    public RoomTypeDto getRoomType(Long id) {
        RoomType entity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room type with id " + id + " not found"));
        return RoomTypeMapper.toDto(entity);
    }

    public List<RoomTypeDto> listRoomTypesForHotel(Long hotelId) {
        List<RoomType> types = roomTypeRepository.findByHotelId(hotelId);
        return types.stream().map(RoomTypeMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Helper used by availability/booking modules which need the entity itself.
     */
    public RoomType getRoomTypeEntity(Long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room type with id " + id + " not found"));
    }
}

