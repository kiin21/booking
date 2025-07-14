package org.example.booking.app.service;

import lombok.RequiredArgsConstructor;
import org.example.booking.app.dto.response.ApiError;
import org.example.booking.app.dto.response.Meta;
import org.example.booking.app.dto.response.ResponseDTO;
import org.example.booking.domain.common.constant.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ResponseFactory {

    @Value("${spring.application.name}")
    String appName;

    public ResponseDTO response(ResponseCode responseCode) {
        Meta meta = Meta.builder()
                .status(responseCode.getType())
                .serviceId(appName)
                .build();

        return new ResponseDTO(meta, null);
    }

    public ResponseDTO response(Object payload) {
        Meta meta = Meta.builder()
                .status(ResponseCode.SUCCESS.getType())
                .serviceId(appName)
                .build();

        return new ResponseDTO(meta, payload);
    }

    public ResponseDTO response(ResponseCode responseCode, Object payload) {
        Meta meta = Meta.builder()
                .status(responseCode.getType())
                .serviceId(appName)
                .build();

        return new ResponseDTO(meta, payload);
    }

    public ResponseDTO invalidParams(Collection<ApiError> errors) {
        Meta meta = Meta.builder()
                .status(ResponseCode.INVALID_PARAMS.getType())
                .serviceId(appName)
                .errors(errors)
                .build();

        return new ResponseDTO(meta, null);
    }
}
