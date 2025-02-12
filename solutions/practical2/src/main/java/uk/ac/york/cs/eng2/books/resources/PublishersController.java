package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.dto.PublisherCreateDTO;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller(PublishersController.PREFIX)
public class PublishersController {
  public static final String PREFIX = "/publishers";

  @Inject
  private PublisherRepository repo;

  @Get
  public List<Publisher> list() {
    return repo.findAll();
  }

  @Get("/{id}")
  public Publisher get(@PathVariable long id) {
    return repo.findById(id).orElse(null);
  }

  @Post
  public HttpResponse<Void> create(@Body PublisherCreateDTO dto) {
    Publisher publisher = new Publisher();
    publisher.setName(dto.getName());
    publisher = repo.save(publisher);
    return HttpResponse.created(URI.create(PREFIX + "/" + publisher.getId()));
  }

  @Transactional
  @Put("/{id}")
  public HttpResponse<Void> update(@Body PublisherCreateDTO dto, @PathVariable long id) {
    @NonNull Optional<Publisher> oPublisher = repo.findById(id);
    if (!oPublisher.isPresent()) {
      return HttpResponse.notFound();
    } else {
      Publisher publisher = oPublisher.get();
      publisher.setName(dto.getName());
      publisher = repo.save(publisher);
      return HttpResponse.noContent();
    }
  }

  @Transactional
  @Delete("/{id}")
  public HttpResponse<Void> delete(@PathVariable long id) {
    @NonNull Optional<Publisher> oPublisher = repo.findById(id);
    if (!oPublisher.isPresent()) {
      return HttpResponse.notFound();
    }

    Publisher publisher = oPublisher.get();
    repo.delete(publisher);
    return HttpResponse.noContent();
  }
}
