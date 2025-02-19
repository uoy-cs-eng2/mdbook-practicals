package uk.ac.york.cs.eng2.checkinstats.resources;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import uk.ac.york.cs.eng2.checkinstats.domain.PartitionedCheckinStat;
import uk.ac.york.cs.eng2.checkinstats.repositories.PartitionedCheckinStatRepository;

import java.util.HashMap;
import java.util.Map;

@Controller("/stats")
public class CheckinStatsController {

  @Inject
  private PartitionedCheckinStatRepository repo;

  @Get
  public Map<String, Long> getStats() {
    Map<String, Long> stats = new HashMap<>();
    for (PartitionedCheckinStat stat : repo.findAll()) {
      stats.compute(stat.getName(), (k, v) -> v == null ? stat.getValue() : v + stat.getValue());
    }
    return stats;
  }

}
