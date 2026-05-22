package com.swer313.hotel.monolith.catalog.controller;

import com.swer313.hotel.monolith.catalog.dto.HotelCreateUpdateRequest;
import com.swer313.hotel.monolith.catalog.dto.HotelDto;
import com.swer313.hotel.monolith.catalog.dto.HotelFilterRequest;
import com.swer313.hotel.monolith.catalog.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for managing and browsing hotels.
 * In a real system we would protect the write operations with authentication/authorization.
 */
@RestController
@RequestMapping("/api/catalog/hotels")
@Tag(name = "Hotel Catalog", description = "Browse and manage hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new hotel (admin/manager)")
    public HotelDto createHotel(@Valid @RequestBody HotelCreateUpdateRequest request) {
        return hotelService.createHotel(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing hotel (admin/manager)")
    public HotelDto updateHotel(@PathVariable Long id,
                                @Valid @RequestBody HotelCreateUpdateRequest request) {
        return hotelService.updateHotel(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a hotel (admin/manager)")
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details for a specific hotel")
    public HotelDto getHotel(@PathVariable Long id) {
        return hotelService.getHotel(id);
    }

    @GetMapping
    @Operation(summary = "Browse hotels with optional city filter and pagination")
    public Page<HotelDto> listHotels(@RequestParam(value = "city", required = false) String city,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return hotelService.listHotels(city, page, size);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter hotels with multiple criteria")
    public Page<HotelDto> filterHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer starRatingMin,
            @RequestParam(required = false) Integer starRatingMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        HotelFilterRequest filter = new HotelFilterRequest();
        filter.setCity(city);
        filter.setName(name);
        filter.setStarRatingMin(starRatingMin);
        filter.setStarRatingMax(starRatingMax);

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return hotelService.filterHotels(filter, pageable);
    }
}

