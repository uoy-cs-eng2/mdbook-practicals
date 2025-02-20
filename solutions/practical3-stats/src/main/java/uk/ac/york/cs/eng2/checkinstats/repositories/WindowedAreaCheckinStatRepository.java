package uk.ac.york.cs.eng2.checkinstats.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import uk.ac.york.cs.eng2.checkinstats.domain.WindowedAreaCheckinStat;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface WindowedAreaCheckinStatRepository extends CrudRepository<WindowedAreaCheckinStat, Long> {

  Optional<WindowedAreaCheckinStat> findByAreaAndWindowStartAtAndName(int area, Instant windowStartAt, String name);

}
