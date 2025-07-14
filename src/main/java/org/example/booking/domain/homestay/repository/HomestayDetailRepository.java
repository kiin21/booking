package org.example.booking.domain.homestay.repository;

import org.example.booking.domain.homestay.dto.request.HomestaySearchRequest;
import org.example.booking.domain.homestay.dto.response.HomestayDetail;

import java.util.List;

public interface HomestayDetailRepository {


    List<HomestayDetail> searchHomestays(HomestaySearchRequest request);

}
