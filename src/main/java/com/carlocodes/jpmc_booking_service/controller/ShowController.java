package com.carlocodes.jpmc_booking_service.controller;

import com.carlocodes.jpmc_booking_service.dto.ShowDto;
import com.carlocodes.jpmc_booking_service.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/show")
public class ShowController {
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping("/admin/setup")
    public ResponseEntity<ShowDto> setup(@RequestBody ShowDto showDto) throws Exception {
        return ResponseEntity.ok(showService.setup(showDto.getRows(),
                showDto.getSeatsPerRow(),
                showDto.getCancellationWindowInMinutes()));
    }
}
