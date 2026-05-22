package com.swer313.hotel.monolith.catalog.controller;

import com.swer313.hotel.monolith.catalog.dto.RoomTypeCreateUpdateRequest;
import com.swer313.hotel.monolith.catalog.dto.RoomTypeDto;
import com.swer313.hotel.monolith.catalog.service.RoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for managing room types inside a hotel.
 */
@RestController
@RequestMapping("/api/catalog/room-types")
@Tag(name = "Room Types", description = "Manage room types within hotels")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new room type for a hotel")
    public RoomTypeDto createRoomType(@Valid @RequestBody RoomTypeCreateUpdateRequest request) {
        return roomTypeService.createRoomType(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing room type")
    public RoomTypeDto updateRoomType(@PathVariable Long id,
                                      @Valid @RequestBody RoomTypeCreateUpdateRequest request) {
        return roomTypeService.updateRoomType(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a room type")
    public void deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteRoomType(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details for a specific room type")
    public RoomTypeDto getRoomType(@PathVariable Long id) {
        return roomTypeService.getRoomType(id);
    }

    @GetMapping("/by-hotel/{hotelId}")
    @Operation(summary = "List all room types for a specific hotel")
    public List<RoomTypeDto> listRoomTypes(@PathVariable Long hotelId) {
        return roomTypeService.listRoomTypesForHotel(hotelId);
    }
}

