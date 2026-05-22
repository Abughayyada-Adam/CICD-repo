package com.swer313.hotel.monolith.catalog.service;

import com.swer313.hotel.monolith.catalog.Specification.HotelSpecifications;
import com.swer313.hotel.monolith.catalog.dto.HotelCreateUpdateRequest;
import com.swer313.hotel.monolith.catalog.dto.HotelDto;
import com.swer313.hotel.monolith.catalog.dto.HotelFilterRequest;
import com.swer313.hotel.monolith.catalog.mapper.HotelMapper;
import com.swer313.hotel.monolith.catalog.model.Hotel;
import com.swer313.hotel.monolith.catalog.repository.HotelRepository;
import com.swer313.hotel.monolith.common.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service that encapsulates all hotel‑related use cases for the catalog module.
 */
@Service
@Transactional
public class HotelService {

    private final HotelRepository hotelRepository;


    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * Create a new hotel from an admin/manager request.
     */
    public HotelDto createHotel(HotelCreateUpdateRequest request) {
        Hotel entity = new Hotel();
        HotelMapper.updateEntityFromRequest(request, entity);
        Hotel saved = hotelRepository.save(entity);
        return HotelMapper.toDto(saved);
    }

    /**
     * Update an existing hotel by id.
     */
    public HotelDto updateHotel(Long id, HotelCreateUpdateRequest request) {
        Hotel existing = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hotel with id " + id + " not found"));
        HotelMapper.updateEntityFromRequest(request, existing);
        return HotelMapper.toDto(existing);
    }

    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new NotFoundException("Hotel with id " + id + " not found");
        }
        hotelRepository.deleteById(id);
    }

    public HotelDto getHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hotel with id " + id + " not found"));
        return HotelMapper.toDto(hotel);
    }

    /**
     * List hotels with optional city filter and pagination.
     */
    public Page<HotelDto> listHotels(String cityFilter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Hotel> hotels;
        if (cityFilter == null || cityFilter.isBlank()) {
            hotels = hotelRepository.findAll(pageable);
        } else {
            hotels = hotelRepository.findByCityContainingIgnoreCase(cityFilter, pageable);
        }
        return hotels.map(HotelMapper::toDto);
    }

    /**
     * Helper used by other modules (availability/booking) that need the entity itself.
     */
    public Hotel getHotelEntity(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hotel with id " + id + " not found"));
    }


    public Page<HotelDto> filterHotels(HotelFilterRequest filter, Pageable pageable) {
        Specification<Hotel> spec = Specification.where(HotelSpecifications.byCity(filter.getCity()))
                .and(HotelSpecifications.byName(filter.getName()))
                .and(HotelSpecifications.byStarRatingMin(filter.getStarRatingMin()))
                .and(HotelSpecifications.byStarRatingMax(filter.getStarRatingMax()))
                .and(HotelSpecifications.byAmenities(filter.getAmenities()))
                .and(HotelSpecifications.byPriceRange(filter.getMinPrice(), filter.getMaxPrice()));

        Page<Hotel> hotels = hotelRepository.findAll(spec, pageable);
        return hotels.map(HotelMapper::toDto);
    }
}

