package com.itexus.controller;

import com.itexus.dto.GenreDTO;
import com.itexus.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GenreDTO>> findAllGenres() {
        List<GenreDTO> genreDTO = genreService.findAllGenres();
        return ResponseEntity.ok(genreDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> findByIdGenre(@PathVariable Long id) {
        GenreDTO genreDTO = genreService.findByIdGenre(id);
        return ResponseEntity.ok(genreDTO);
    }

    @PostMapping
    public ResponseEntity<GenreDTO> saveGenre(@RequestBody GenreDTO genreDTO) {
        return new ResponseEntity<>(genreService.saveGenre(genreDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GenreDTO> updateGenre(@RequestBody GenreDTO genreDTO) {
        return new ResponseEntity<>(genreService.updateGenre(genreDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }
}
