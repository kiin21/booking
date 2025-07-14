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
            WITH cte AS (
                SELECT
                    ha.homestay_id AS homestay_id,
                    ha.date AS date,
                    ha.date - (ROW_NUMBER() OVER(PARTITION BY ha.homestay_id ORDER BY ha.date) * INTERVAL '1 days') AS group_id
                FROM homestay_availabilities ha
                JOIN homestays h ON h.id = ha.homestay_id
                WHERE ha.status = :status
            ),
            consecutive_days AS (
                SELECT
                    homestay_id,
                    MIN(date) AS start_date,
                    MAX(date) AS end_date,
                    MAX(date) - MIN(date) + 1 AS days
                FROM cte
                GROUP BY homestay_id, group_id
                HAVING MIN(date) <= DATE :checkinDate AND DATE :checkoutDate <= MAX(date)
            ),
            destination AS (
                SELECT ST_Transform(ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), 3857) AS geom
            )
            SELECT h.id, cd.start_date, cd.end_date, h.geom, d.geom, h.geom <-> d.geom AS distant, cd.days
            FROM homestays h
                CROSS JOIN destination d
                JOIN consecutive_days cd ON h.id = cd.homestay_id
            WHERE ST_DWithin(h.geom, d.geom, :radius)
              AND h.guests >= :guests
            ORDER BY h.geom <-> d.geom;
            """, nativeQuery = true)
    List<HomestayDTO> searchHomestay(@Param("longitude") Double longitude,
                                     @Param("latitude") Double latitude,
                                     @Param("radius") Double radius,
                                     @Param("checkinDate") LocalDate checkinDate,
                                     @Param("checkoutDate") LocalDate checkoutDate,
                                     @Param("nights") Integer nights,
                                     @Param("guests") Integer guests,
                                     @Param("status") Integer status);
}
