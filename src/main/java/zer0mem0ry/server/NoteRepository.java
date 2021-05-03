package zer0mem0ry.server;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByTitleContaining(String title);
    List<Note> findByPublished(boolean published);
}