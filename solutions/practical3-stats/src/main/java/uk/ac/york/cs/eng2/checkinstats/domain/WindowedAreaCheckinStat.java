package uk.ac.york.cs.eng2.checkinstats.domain;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

@Serdeable
@Entity
public class WindowedAreaCheckinStat {
  @Id
  @GeneratedValue
  private Long id;

  @Column
  private Integer area;

  @Column
  private Instant windowStartAt;

  @Column
  private String name;

  @Column
  private Long value;

  public WindowedAreaCheckinStat() {
    // no-arg constructor
  }

  public WindowedAreaCheckinStat(int area, Instant windowStartAt, String name) {
    this.area = area;
    this.windowStartAt = windowStartAt;
    this.name = name;
    this.value = 0L;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getArea() {
    return area;
  }

  public void setArea(Integer area) {
    this.area = area;
  }

  public Instant getWindowStartAt() {
    return windowStartAt;
  }

  public void setWindowStartAt(Instant windowStartAt) {
    this.windowStartAt = windowStartAt;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "WindowedAreaCheckinStat{" +
        "id=" + id +
        ", area=" + area +
        ", windowStartAt=" + windowStartAt +
        ", name='" + name + '\'' +
        ", value=" + value +
        '}';
  }
}
