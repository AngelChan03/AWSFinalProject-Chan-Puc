package com.aws.awsproject.controller;

import com.aws.awsproject.dto.ProfessorDTO;
import com.aws.awsproject.entity.Professor;
import com.aws.awsproject.service.ProfessorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<Professor>> getAllProfessors() {
        return ResponseEntity.ok(professorService.getAllProfessors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Integer id) {
        return ResponseEntity.ok(professorService.getProfessorById(id));
    }

    @PostMapping
    public ResponseEntity<Professor> createProfessor(@Valid @RequestBody ProfessorDTO professorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.createProfessor(professorDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Integer id, @Valid @RequestBody ProfessorDTO professorDTO) {
        return ResponseEntity.ok(professorService.updateProfessor(id, professorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Integer id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.ok().build();
    }
}
