package chan.AWSFinalProject.controllers;

import chan.AWSFinalProject.models.Alumno;
import chan.AWSFinalProject.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AlumnoController {

    private List<Alumno> alumnos = new ArrayList<>();

    // GET /alumnos - obtener todos
    @GetMapping("/alumnos")
    public ResponseEntity<List<Alumno>> getAllAlumnos() {
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    // GET /alumnos/{id} - obtener por id
    @GetMapping("/alumnos/{id}")
    public ResponseEntity<?> getAlumnoById(@PathVariable int id) {
        Optional<Alumno> alumno = alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (alumno.isPresent()) {
            return new ResponseEntity<>(alumno.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ErrorResponse("Alumno no encontrado", 404),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // POST /alumnos - crear alumno
    @PostMapping("/alumnos")
    public ResponseEntity<?> createAlumno(@RequestBody Alumno nuevoAlumno) {

        // Validaciones
        if (nuevoAlumno.getId() <= 0)
            return new ResponseEntity<>(
                    new ErrorResponse("El ID debe ser un número positivo", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (isNullOrEmpty(nuevoAlumno.getNombres()) || isNullOrEmpty(nuevoAlumno.getApellidos()))
            return new ResponseEntity<>(
                    new ErrorResponse("Los nombres y apellidos no pueden estar vacíos", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (isNullOrEmpty(nuevoAlumno.getMatricula()))
            return new ResponseEntity<>(
                    new ErrorResponse("La matrícula no puede estar vacía", 400),
                    HttpStatus.BAD_REQUEST
            );

        // Promedio entre 0 y 1
        if (nuevoAlumno.getPromedio() < 0 || nuevoAlumno.getPromedio() > 1)
            return new ResponseEntity<>(
                    new ErrorResponse("El promedio debe estar entre 0 y 1", 400),
                    HttpStatus.BAD_REQUEST
            );

        // ID duplicado
        boolean exists = alumnos.stream().anyMatch(a -> a.getId() == nuevoAlumno.getId());
        if (exists)
            return new ResponseEntity<>(
                    new ErrorResponse("Ya existe un alumno con ese ID", 400),
                    HttpStatus.BAD_REQUEST
            );

        alumnos.add(nuevoAlumno);
        return new ResponseEntity<>(nuevoAlumno, HttpStatus.CREATED);
    }

    // PUT /alumnos/{id} - actualizar alumno
    @PutMapping("/alumnos/{id}")
    public ResponseEntity<?> updateAlumno(@PathVariable int id, @RequestBody Alumno alumnoActualizado) {

        Optional<Alumno> alumnoOpt = alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (!alumnoOpt.isPresent())
            return new ResponseEntity<>(
                    new ErrorResponse("Alumno no encontrado", 404),
                    HttpStatus.NOT_FOUND
            );

        Alumno alumno = alumnoOpt.get();

        // Validaciones
        if (isNullOrEmpty(alumnoActualizado.getNombres()) || isNullOrEmpty(alumnoActualizado.getApellidos()))
            return new ResponseEntity<>(
                    new ErrorResponse("Los nombres y apellidos no pueden estar vacíos", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (isNullOrEmpty(alumnoActualizado.getMatricula()))
            return new ResponseEntity<>(
                    new ErrorResponse("La matrícula no puede estar vacía", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (alumnoActualizado.getPromedio() < 0 || alumnoActualizado.getPromedio() > 1)
            return new ResponseEntity<>(
                    new ErrorResponse("El promedio debe estar entre 0 y 1", 400),
                    HttpStatus.BAD_REQUEST
            );

        // Actualizar datos
        alumno.setNombres(alumnoActualizado.getNombres());
        alumno.setApellidos(alumnoActualizado.getApellidos());
        alumno.setMatricula(alumnoActualizado.getMatricula());
        alumno.setPromedio(alumnoActualizado.getPromedio());

        return new ResponseEntity<>(alumno, HttpStatus.OK);
    }

    // DELETE /alumnos/{id}
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<?> deleteAlumno(@PathVariable int id) {

        Optional<Alumno> alumnoOpt = alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (!alumnoOpt.isPresent())
            return new ResponseEntity<>(
                    new ErrorResponse("Alumno no encontrado", 404),
                    HttpStatus.NOT_FOUND
            );

        alumnos.remove(alumnoOpt.get());
        return new ResponseEntity<>(
                new ErrorResponse("Alumno eliminado correctamente", 200),
                HttpStatus.OK
        );
    }

    // Helper
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}