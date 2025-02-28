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
      /*
       * map.compute(key, keyval_function) is a shorthand way to build up a
       * calculation within a certain key in a map. More information is available
       * in the official Javadocs:
       *
       *  https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#compute-K-java.util.function.BiFunction-
       *
       * The keyval_function is a BiFunction<K, V, V> which takes the key and the existing
       * value for that key in the map (or null if it does not exist yet), and returns
       * the new value.
       *
       * In the code below, we are using a lambda expression that produces the desired
       * BiFunction<K, V> object. For more information on lambda expressions, consult
       * the Learn section of the dev.java website:
       *
       *   https://dev.java/learn/lambdas/first-lambdas/
       */
      stats.compute(stat.getName(), (k, v) -> v == null ? stat.getValue() : v + stat.getValue());
    }
    return stats;
  }

  @Get("/windowed")
  public Map<Integer, Map<String, Map<String, Long>>> getWindowedStats() {
    Map<Integer, Map<String, Map<String, Long>>> stats = new HashMap<>();

    for (WindowedAreaCheckinStat stat : winRepo.findAll()) {
      /*
       * map.computeIfAbsent(key, value_from_key_function) is a simplified version of
       * compute() which will get the existing value for that key if there is one, or
       * set and return the value produced by the given function if the key does not
       * exist in the map yet. More information is available in the official Javadocs:
       *
       *   https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#computeIfAbsent-K-java.util.function.Function-
       *
       * value_from_key_function must be a Function<K, V> object which takes the key
       * and produces the initial value for that key. We use a lambda expression for
       * this as well.
       */
      stats.computeIfAbsent(stat.getArea(), k -> new HashMap<>())
          .computeIfAbsent(stat.getName(), k -> new HashMap<>())
          .put(stat.getWindowStartAt().toString(), stat.getValue());
    }

    return stats;
  }


}
