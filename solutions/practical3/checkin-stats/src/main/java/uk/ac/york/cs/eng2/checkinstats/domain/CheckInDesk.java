package uk.ac.york.cs.eng2.checkinstats.domain;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

@Serdeable
@Entity
public class CheckInDesk {
  @Id
  @GeneratedValue
  private Long id;

  @Column
  private Long deskId;

  @Column
  private Instant checkinStartedAt;

  @Column
  private Instant lastStatusAt;

  @Column
  private Boolean outOfOrder;

  public CheckInDesk() {
    // no-arg constructor
  }

  public CheckInDesk(long deskId) {
    this.deskId = deskId;
    this.outOfOrder = false;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDeskId() {
    return deskId;
  }

  public void setDeskId(Long deskId) {
    this.deskId = deskId;
  }

  public Instant getCheckinStartedAt() {
    return checkinStartedAt;
  }

  public void setCheckinStartedAt(Instant timestamp) {
    this.checkinStartedAt = timestamp;
  }

  public Instant getLastStatusAt() {
    return lastStatusAt;
  }

  public void setLastStatusAt(Instant lastStatusAt) {
    this.lastStatusAt = lastStatusAt;
  }

  public Boolean getOutOfOrder() {
    return outOfOrder;
  }

  public void setOutOfOrder(Boolean outOfOrder) {
    this.outOfOrder = outOfOrder;
  }
}
