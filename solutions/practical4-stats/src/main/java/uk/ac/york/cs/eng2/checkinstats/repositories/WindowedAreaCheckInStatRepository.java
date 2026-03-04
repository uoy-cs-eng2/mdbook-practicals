package uk.ac.york.cs.eng2.checkinstats.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import uk.ac.york.cs.eng2.checkinstats.domain.WindowedAreaCheckInStat;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface WindowedAreaCheckInStatRepository extends CrudRepository<WindowedAreaCheckInStat, Long> {

  Optional<WindowedAreaCheckInStat> findByAreaAndWindowStartAtAndName(int area, Instant windowStartAt, String name);

}
