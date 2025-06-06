package ddd.darayo.festival.domain.repository.projection;

import ddd.darayo.festival.domain.constant.ParticipationType;
import ddd.darayo.festival.domain.constant.ReservationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface PerformanceDetailProjection {
    // Performance fields
    Long getPerformanceId();
    String getPerformanceName();
    String getPlaceName();
    String getPlaceAddress();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getPosterUrl();
    String getBanGoods();
    String getTransportationInfo();
    String getPerformanceRemark();

    // Timetable fields (nullable)
    Long getTimetableId();
    LocalDate getTimetablePerformanceDate();
    LocalTime getTimetableStartTime();
    LocalTime getTimetableEndTime();
    String getTimetablePerformanceHall();

    // TimetableArtist fields (nullable, via Timetable)
    Long getTimetableArtistId();
    Long getTimetableArtistArtistId();
    String getTimetableArtistName();
    ParticipationType getTimetableArtistType();

    // ReservationInfo fields (nullable)
    Long getReservationInfoId();
    LocalDateTime getReservationInfoOpenDateTime();
    LocalDateTime getReservationInfoCloseDateTime();
    String getReservationInfoTicketURL();
    ReservationType getReservationInfoType();
    String getReservationInfoRemark();

    // PerformanceArtist fields (nullable, Performance에 직접 연결된 아티스트)
    Long getPerformanceArtistId();
    String getPerformanceArtistDisplayName();
    LocalDate getPerformanceArtistDate();
} 