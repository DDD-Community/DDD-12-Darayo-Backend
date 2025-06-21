package ddd.darayo.festival.presentation.place.exchanges;

import java.util.List;

public record AddPlaceReq(
        String placeName,
        String address,
        List<String> placeHalls
) {
}
