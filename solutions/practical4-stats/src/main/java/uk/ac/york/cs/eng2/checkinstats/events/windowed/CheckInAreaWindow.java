package uk.ac.york.cs.eng2.checkinstats.events.windowed;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CheckInAreaWindow(int area, long windowStartEpochMillis) {
}
