package ddd.darayo.festival.domain.exception.constant;

public enum PerformanceError implements ErrorInfo{
    PERFORMANCE_NOT_EXIST("해당하는 공연 정보가 없습니다.");

    PerformanceError(String description) {
        this.description = description;
    }

    private final String description;

    @Override
    public String getDesc() {
        return description;
    }
}
