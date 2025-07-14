package org.example.booking.rest;

import lombok.RequiredArgsConstructor;
import org.example.booking.app.dto.response.ResponseDTO;
import org.example.booking.app.service.ResponseFactory;
import org.example.booking.domain.homestay.dto.HomestayDTO;
import org.example.booking.domain.homestay.dto.request.HomestaySearchRequest;
import org.example.booking.domain.homestay.entity.Homestay;
import org.example.booking.domain.homestay.service.HomestayService;
import org.example.booking.infrastructure.util.DateUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/homestays")
@RequiredArgsConstructor
public class HomestayController {

    private final HomestayService service;
    private final ResponseFactory responseFactory;


    @GetMapping("/{id}")
    public ResponseDTO getHomestayById(@PathVariable String id) {
        Homestay response = service.getHomestayById(Long.valueOf(id));
        return responseFactory.response(response);
    }


    @GetMapping
    public ResponseDTO searchHomestay(@RequestParam(value = "longitude") Double longitude,
                                      @RequestParam(value = "latitude") Double latitude,
                                      @RequestParam(value = "radius") Double radius,
                                      @RequestParam(value = "checkin_date") String checkinDate,
                                      @RequestParam(value = "checkout_date") String checkoutDate,
                                      @RequestParam(value = "guests") Integer guests) {

        HomestaySearchRequest request = HomestaySearchRequest.builder()
                .longitude(longitude)
                .latitude(latitude)
                .radius(radius)
                .checkinDate(DateUtil.parse(checkinDate))
                .checkoutDate(DateUtil.parse(checkoutDate))
                .guests(guests)
                .build();

        List<HomestayDTO> result = service.searchHomestays(request);

        return responseFactory.response(result);
    }
}
