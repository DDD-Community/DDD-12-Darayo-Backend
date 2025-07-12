package ddd.darayo.festival.application.usecase.common;

public interface UseCase<P extends Params, R> {
    R execute(P params);
}
