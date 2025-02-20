package uk.ac.york.cs.eng2.checkinstats.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import uk.ac.york.cs.eng2.checkinstats.domain.CheckInDesk;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInDeskRepository extends CrudRepository<CheckInDesk, Long> {

  Optional<CheckInDesk> findByDeskId(long deskId);

  List<CheckInDesk> findByOutOfOrder(boolean outOfOrder);

  List<CheckInDesk> findByOutOfOrderAndCheckinStartedAtIsNotNull(boolean outOfOrder);

  List<CheckInDesk> findByLastStatusAtBefore(Instant before);
}
