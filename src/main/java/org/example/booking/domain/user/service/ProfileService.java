package org.example.booking.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.booking.domain.user.entity.Profile;
import org.example.booking.domain.user.repository.ProfileRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;


    public Profile getProfileByUserId(Long userId) {
        return repository.findById(userId).orElse(null);
    }

}
