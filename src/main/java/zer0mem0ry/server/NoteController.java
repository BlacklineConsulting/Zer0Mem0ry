package zer0mem0ry.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")

public class NoteController {

    @Autowired
    NoteRepository noteRepository;


    @PostMapping("/notes")
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        try {
            Note _note = noteRepository.save(new Note(note.getTitle(), note.getDescription(), false));
            return new ResponseEntity<>(_note, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable("id") String id, @RequestBody Note note) {
        Optional<Note> noteData = noteRepository.findById(id);

        if (noteData.isPresent()) {
            Note _note = noteData.get();
            _note.setTitle(note.getTitle());
            _note.setDescription(note.getDescription());
            _note.setPublished(note.isPublished());
            return new ResponseEntity<>(noteRepository.save(_note), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/notes/{id}")
    public ResponseEntity<HttpStatus> deleteNote(@PathVariable("id") String id) {
        try {
            noteRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/notes")
    public ResponseEntity<HttpStatus> deleteAllNotes() {
        try {
            noteRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/notes")
    public ResponseEntity<List<Note>> getAllNotes(@RequestParam(required = false) String title) {
        try {
            List<Note> notes = new ArrayList<Note>();

            if (title == null)
                noteRepository.findAll().forEach(notes::add);
            else
                noteRepository.findByTitleContaining(title).forEach(notes::add);

            if (notes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/notes/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") String id) {
        Optional<Note> noteData = noteRepository.findById(id);

        if (noteData.isPresent()) {
            return new ResponseEntity<>(noteData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/notes/published")
    public ResponseEntity<List<Note>> findByPublished() {
        try {
            List<Note> notes = noteRepository.findByPublished(true);

            if (notes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
