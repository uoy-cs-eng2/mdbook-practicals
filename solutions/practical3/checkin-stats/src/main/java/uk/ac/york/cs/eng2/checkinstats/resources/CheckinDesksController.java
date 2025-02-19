package uk.ac.york.cs.eng2.checkinstats.resources;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import uk.ac.york.cs.eng2.checkinstats.domain.CheckInDesk;
import uk.ac.york.cs.eng2.checkinstats.repositories.CheckInDeskRepository;

import java.util.Collection;

@Controller("/desks")
public class CheckinDesksController {
  @Inject
  private CheckInDeskRepository repo;

  @Get
  public Collection<CheckInDesk> list() {
    return repo.findAll();
  }

  @Get("/outOfOrder")
  public Collection<CheckInDesk> outOfOrder() {
    return repo.findByOutOfOrder(true);
  }

  @Get("/stuck")
  public Collection<CheckInDesk> stuck() {
    return repo.findByOutOfOrderAndCheckinStartedAtIsNotNull(true);
  }
}
