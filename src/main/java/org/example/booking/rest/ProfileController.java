package org.example.booking.rest;

import lombok.RequiredArgsConstructor;
import org.example.booking.domain.user.entity.Profile;
import org.example.booking.domain.user.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final ProfileService service;

    @GetMapping("/{id}")
    public Profile getProfileById(@PathVariable Long id) {
        return service.getProfileByUserId(id);
    }
}

