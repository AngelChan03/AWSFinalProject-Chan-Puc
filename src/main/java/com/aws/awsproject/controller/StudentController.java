package com.aws.awsproject.controller;

import com.aws.awsproject.dto.LoginRequest;
import com.aws.awsproject.dto.SessionRequest;
import com.aws.awsproject.dto.SessionResponse;
import com.aws.awsproject.dto.StudentDTO;
import com.aws.awsproject.entity.SessionAlumno;
import com.aws.awsproject.entity.Student;
import com.aws.awsproject.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alumnos")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Valid @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(studentDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Integer id, @Valid @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/fotoPerfil", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @PathVariable Integer id,
            @RequestParam("foto") MultipartFile file) {
        try {
            Student student = studentService.uploadProfilePicture(id, file);
            Map<String, String> response = new HashMap<>();
            response.put("fotoPerfilUrl", student.getFotoPerfilUrl());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/email")
    public ResponseEntity<Map<String, String>> sendEmail(@PathVariable Integer id) {
        studentService.sendEmail(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email sent successfully");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/session/login")
    public ResponseEntity<SessionResponse> login(
            @PathVariable Integer id,
            @Valid @RequestBody LoginRequest loginRequest) {
        try {
            SessionAlumno session = studentService.login(id, loginRequest.getPassword());
            return ResponseEntity.ok(new SessionResponse(session.getSessionString()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/{id}/session/verify")
    public ResponseEntity<Map<String, Boolean>> verifySession(
            @PathVariable Integer id,
            @Valid @RequestBody SessionRequest sessionRequest) {
        boolean isValid = studentService.verifySession(id, sessionRequest.getSessionString());

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);

        if (isValid) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @PostMapping("/{id}/session/logout")
    public ResponseEntity<Map<String, String>> logout(
            @PathVariable Integer id,
            @Valid @RequestBody SessionRequest sessionRequest) {
        try {
            studentService.logout(id, sessionRequest.getSessionString());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout successful");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

}
