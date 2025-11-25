package chan.AWSFinalProject.controllers;

import chan.AWSFinalProject.models.Profesor;
import chan.AWSFinalProject.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProfesorController {

    private final List<Profesor> profesores = new ArrayList<>();

    @GetMapping("/profesores")
    public ResponseEntity<List<Profesor>> getAllProfesores() {
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }

    @GetMapping("/profesores/{id}")
    public ResponseEntity<?> getProfesorById(@PathVariable int id) {
        Optional<Profesor> profesor = profesores.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (profesor.isPresent()) {
            return new ResponseEntity<>(profesor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ErrorResponse("Profesor no encontrado", 404),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping("/profesores")
    public ResponseEntity<?> createProfesor(@RequestBody Profesor nuevoProfesor) {
        if (nuevoProfesor.getId() <= 0)
            return new ResponseEntity<>(
                    new ErrorResponse("El ID debe ser un número positivo", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (nuevoProfesor.getNumeroEmpleado() <= 0)
            return new ResponseEntity<>(
                    new ErrorResponse("El número de empleado debe ser un número positivo", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (isNullOrEmpty(nuevoProfesor.getNombres()) || isNullOrEmpty(nuevoProfesor.getApellidos()))
            return new ResponseEntity<>(
                    new ErrorResponse("Los nombres y apellidos no pueden estar vacíos", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (nuevoProfesor.getHorasClase() < 0)
            return new ResponseEntity<>(
                    new ErrorResponse("Las horas de clase no pueden ser negativas", 400),
                    HttpStatus.BAD_REQUEST
            );

        boolean exists = profesores.stream().anyMatch(p ->
                p.getId() == nuevoProfesor.getId() ||
                        p.getNumeroEmpleado() == nuevoProfesor.getNumeroEmpleado()
        );

        if (exists)
            return new ResponseEntity<>(
                    new ErrorResponse("Ya existe un profesor con ese ID o número de empleado", 400),
                    HttpStatus.BAD_REQUEST
            );

        profesores.add(nuevoProfesor);
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }

    @PutMapping("/profesores/{id}")
    public ResponseEntity<?> updateProfesor(@PathVariable int id, @RequestBody Profesor profesorActualizado) {
        Optional<Profesor> profesorOpt = profesores.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (!profesorOpt.isPresent()) {
            return new ResponseEntity<>(
                    new ErrorResponse("Profesor no encontrado", 404),
                    HttpStatus.NOT_FOUND
            );
        }

        Profesor profesor = profesorOpt.get();

        if (isNullOrEmpty(profesorActualizado.getNombres()) || isNullOrEmpty(profesorActualizado.getApellidos()))
            return new ResponseEntity<>(
                    new ErrorResponse("Los nombres y apellidos no pueden estar vacíos", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (profesorActualizado.getNumeroEmpleado() <= 0)
            return new ResponseEntity<>(
                    new ErrorResponse("El número de empleado debe ser positivo", 400),
                    HttpStatus.BAD_REQUEST
            );

        if (profesorActualizado.getHorasClase() < 0)
            return new ResponseEntity<>(
                    new ErrorResponse("Las horas de clase no pueden ser negativas", 400),
                    HttpStatus.BAD_REQUEST
            );

        profesor.setNombres(profesorActualizado.getNombres());
        profesor.setApellidos(profesorActualizado.getApellidos());
        profesor.setNumeroEmpleado(profesorActualizado.getNumeroEmpleado());
        profesor.setHorasClase(profesorActualizado.getHorasClase());

        return new ResponseEntity<>(profesor, HttpStatus.OK);
    }

    @DeleteMapping("/profesores/{id}")
    public ResponseEntity<?> deleteProfesor(@PathVariable int id) {
        Optional<Profesor> profesorOpt = profesores.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (!profesorOpt.isPresent()) {
            return new ResponseEntity<>(
                    new ErrorResponse("Profesor no encontrado", 404),
                    HttpStatus.NOT_FOUND
            );
        }

        profesores.remove(profesorOpt.get());
        return new ResponseEntity<>(
                new ErrorResponse("Profesor eliminado correctamente", 200),
                HttpStatus.OK
        );
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}