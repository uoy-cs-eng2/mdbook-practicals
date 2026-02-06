package uk.ac.york.cs.eng2.checkinstats.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.checkinstats.domain.CheckInDesk;
import uk.ac.york.cs.eng2.checkinstats.repositories.CheckInDeskRepository;

import java.time.Instant;

@KafkaListener(groupId = "checkin-status-timestamps")
public class StatusUpdateConsumer {

  @Inject
  private CheckInDeskRepository repo;

  @Transactional
  @Topic(CheckinTopics.TOPIC_STATUS)
  public void statusUpdate(@KafkaKey long deskId, TerminalInfo tInfo, long timestamp) {
    CheckInDesk desk = repo.findByDeskId(deskId).orElse(new CheckInDesk(deskId));
    desk.setLastStatusAt(Instant.ofEpochMilli(timestamp));
    repo.save(desk);
  }

}
