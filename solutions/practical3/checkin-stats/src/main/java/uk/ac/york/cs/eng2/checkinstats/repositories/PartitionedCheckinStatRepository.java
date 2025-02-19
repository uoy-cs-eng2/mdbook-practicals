package uk.ac.york.cs.eng2.checkinstats.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import uk.ac.york.cs.eng2.checkinstats.domain.PartitionedCheckinStat;

import java.util.Optional;

@Repository
public interface PartitionedCheckinStatRepository extends CrudRepository<PartitionedCheckinStat, Long> {

  Optional<PartitionedCheckinStat> findByPartitionIdAndName(int partitionId, String name);

}
