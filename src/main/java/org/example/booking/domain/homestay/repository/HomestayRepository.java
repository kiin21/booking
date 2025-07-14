package org.example.booking.domain.homestay.repository;

import org.example.booking.domain.homestay.dto.HomestayDTO;
import org.example.booking.domain.homestay.entity.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface HomestayRepository extends JpaRepository<Homestay, Long> {

    @Query(value = """
            WITH destination AS (
                SELECT ST_Transform(ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), 3857) AS geom
            ),
            available_homestays AS (
                SELECT ha.homestay_id, avg(ha.price) as night_amount, SUM(ha.price) AS total_amount
                FROM homestays_availabilities ha
                WHERE ha.status = :status
                  AND ha.date BETWEEN :checkinDate AND :checkoutDate
                GROUP BY ha.homestay_id
                HAVING COUNT(ha.date) = :nights + 1
            )
            SELECT h.id, h.name, h.description, h.images, h.bedrooms, available.night_amount,
                   available.total_amount, h.address, h.longitude, h.latitude
            FROM homestays h
            INNER JOIN available_homestays available ON h.id = available.homestay_id
            JOIN destination d ON TRUE
            WHERE ST_DWithin(h.geom, d.geom, :radius) AND h.guests >= :guests
            ORDER BY h.geom <-> d.geom;
            """, nativeQuery = true)
    List<HomestayDTO> searchHomestay(@Param("longitude") Double longitude,
                                     @Param("latitude") Double latitude,
                                     @Param("radius") Double radius_meters,
                                     @Param("checkinDate") LocalDate checkinDate,
                                     @Param("checkoutDate") LocalDate checkoutDate,
                                     @Param("nights") Integer nights,
                                     @Param("guests") Integer guests,
                                     @Param("status") Integer status);
}
