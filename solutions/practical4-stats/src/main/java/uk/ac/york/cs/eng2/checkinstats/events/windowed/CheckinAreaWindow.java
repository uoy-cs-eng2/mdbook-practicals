package uk.ac.york.cs.eng2.checkinstats.events.windowed;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CheckinAreaWindow(int area, long windowStartEpochMillis) {
}
