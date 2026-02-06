package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.configuration.kafka.annotation.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.checkinstats.domain.CheckInDesk;
import uk.ac.york.cs.eng2.checkinstats.repositories.CheckInDeskRepository;

import java.time.Instant;

@KafkaListener(groupId="checkin-desks", threads = 3, offsetReset = OffsetReset.EARLIEST)
public class CheckinDeskConsumer {

  @Inject
  private CheckInDeskRepository repo;

  @Transactional
  @Topic(CheckinTopics.TOPIC_CHECKIN)
  public void checkInStarted(@KafkaKey long deskId, TerminalInfo tInfo, long timestamp) {
    CheckInDesk desk = repo.findByDeskId(deskId).orElse(new CheckInDesk(deskId));
    desk.setCheckinStartedAt(Instant.ofEpochMilli(timestamp));
    desk.setOutOfOrder(false);
    repo.save(desk);
  }

  @Transactional
  @Topic({CheckinTopics.TOPIC_CANCELLED, CheckinTopics.TOPIC_COMPLETED})
  public void checkInDone(@KafkaKey long deskId, TerminalInfo tInfo, long timestamp) {
    CheckInDesk desk = repo.findByDeskId(deskId).orElse(new CheckInDesk(deskId));
    desk.setCheckinStartedAt(null);
    desk.setOutOfOrder(false);
    repo.save(desk);
  }

  @Transactional
  @Topic(CheckinTopics.TOPIC_OUTOFORDER)
  public void checkInDeskOutOfOrder(@KafkaKey long deskId, TerminalInfo tInfo, long timestamp) {
    CheckInDesk desk = repo.findByDeskId(deskId).orElse(new CheckInDesk(deskId));
    desk.setOutOfOrder(true);
    repo.save(desk);
  }

}
