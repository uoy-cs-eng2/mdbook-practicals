package uk.ac.york.cs.eng2.books.resources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uk.ac.york.cs.eng2.books.domain.Publisher;
import uk.ac.york.cs.eng2.books.dto.PublisherCreateDTO;
import uk.ac.york.cs.eng2.books.repository.PublisherRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Client(PublishersController.PREFIX)
public interface PublishersClient {

    @Get
    List<Publisher> list();

    @Get("/{id}")
    Publisher get(@PathVariable long id);

    @Post
    HttpResponse<Void> create(@Body PublisherCreateDTO dto);

    @Put("/{id}")
    HttpResponse<Void> update(@Body PublisherCreateDTO dto, @PathVariable long id);

    @Delete("/{id}")
    HttpResponse<Void> delete(@PathVariable long id);

}
