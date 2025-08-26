package ddd.darayo.festival.infra.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TouchPerformanceUpdatedAt {
    enum By {
        PERFORMANCE_ID,
        RESERVATION_INFO_ID,
        PERFORMANCE_URL_ID,
        TIMETABLE_ID,
        HALL_ID,
        PLACE_ID
    }

    By by();

    // SpEL to extract key argument (e.g., "#performanceId", "#reservationInfoId")
    String key();

    // SpEL to extract now argument (e.g., "#now"). If empty, current time from Clock will be used
    String now() default "";
}


