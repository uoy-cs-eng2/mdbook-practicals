package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TerminalInfo(boolean stuck, int paperLeft) {

}
