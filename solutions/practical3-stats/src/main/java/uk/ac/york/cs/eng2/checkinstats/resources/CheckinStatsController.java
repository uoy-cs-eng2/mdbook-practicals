package uk.ac.york.cs.eng2.checkinstats.resources;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import uk.ac.york.cs.eng2.checkinstats.domain.PartitionedCheckinStat;
import uk.ac.york.cs.eng2.checkinstats.domain.WindowedAreaCheckinStat;
import uk.ac.york.cs.eng2.checkinstats.repositories.PartitionedCheckinStatRepository;
import uk.ac.york.cs.eng2.checkinstats.repositories.WindowedAreaCheckinStatRepository;

import java.util.HashMap;
import java.util.Map;

@Controller("/stats")
public class CheckinStatsController {

  @Inject
  private PartitionedCheckinStatRepository repo;

  @Inject
  private WindowedAreaCheckinStatRepository winRepo;

  @Get
  public Map<String, Long> getStats() {
    Map<String, Long> stats = new HashMap<>();
    for (PartitionedCheckinStat stat : repo.findAll()) {
      stats.compute(stat.getName(), (k, v) -> v == null ? stat.getValue() : v + stat.getValue());
    }
    return stats;
  }

  @Get("/windowed")
  public Map<Integer, Map<String, Map<String, Long>>> getWindowedStats() {
    Map<Integer, Map<String, Map<String, Long>>> stats = new HashMap<>();

    for (WindowedAreaCheckinStat stat : winRepo.findAll()) {
      stats.computeIfAbsent(stat.getArea(), k -> new HashMap<>())
          .computeIfAbsent(stat.getName(), (k) -> new HashMap<>())
          .put(stat.getWindowStartAt().toString(), stat.getValue());
    }

    return stats;
  }


}
