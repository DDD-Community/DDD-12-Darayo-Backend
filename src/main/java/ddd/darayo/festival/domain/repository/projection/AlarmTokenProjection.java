package ddd.darayo.festival.domain.repository.projection;

public interface AlarmTokenProjection {
    Long getTokenId();
    String getAlarmToken();
    Long getTargetId();
}
