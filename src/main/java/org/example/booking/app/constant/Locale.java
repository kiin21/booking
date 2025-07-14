package org.example.booking.app.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Locale {
    VIETNAME("vn"),
    ENGLISH("en");

    private final String value;
}
