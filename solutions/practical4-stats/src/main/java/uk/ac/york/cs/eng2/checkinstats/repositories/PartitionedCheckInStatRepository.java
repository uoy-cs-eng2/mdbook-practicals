package uk.ac.york.cs.eng2.checkinstats.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import uk.ac.york.cs.eng2.checkinstats.domain.PartitionedCheckInStat;

import java.util.Optional;

@Repository
public interface PartitionedCheckInStatRepository extends CrudRepository<PartitionedCheckInStat, Long> {

  Optional<PartitionedCheckInStat> findByPartitionIdAndName(int partitionId, String name);

}
