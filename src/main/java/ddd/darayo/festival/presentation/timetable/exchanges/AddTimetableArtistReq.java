package ddd.darayo.festival.presentation.timetable.exchanges;

import ddd.darayo.festival.domain.constant.ParticipationType;

public record AddTimetableArtistReq(
        Long artistId,
        ParticipationType participationType
) { }
