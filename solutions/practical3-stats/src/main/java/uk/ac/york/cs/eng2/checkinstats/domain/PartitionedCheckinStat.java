package uk.ac.york.cs.eng2.checkinstats.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PartitionedCheckinStat {
  @Id
  @GeneratedValue
  private Long id;

  @Column
  private Integer partitionId;

  @Column
  private String name;

  @Column
  private Long value;

  public PartitionedCheckinStat() {
    // no-arg constructor
  }

  public PartitionedCheckinStat(int partition, String name) {
    this.partitionId = partition;
    this.name = name;
    this.value = 0L;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getPartitionId() {
    return partitionId;
  }

  public void setPartitionId(Integer partitionId) {
    this.partitionId = partitionId;
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
}
