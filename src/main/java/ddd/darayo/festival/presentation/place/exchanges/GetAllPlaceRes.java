package ddd.darayo.festival.presentation.place.exchanges;

import java.util.List;

public record GetAllPlaceRes(
        Long id,
        String placeName,
        String address,
        List<HallInfo> halls
) {
    public record HallInfo(
            Long id,
            String name
    ) {}
}
