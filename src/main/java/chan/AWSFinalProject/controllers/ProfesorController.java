package chan.AWSFinalProject.controllers;

import chan.AWSFinalProject.models.Profesor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProfesorController {

    private final List<Profesor> profesores = new ArrayList<>();

    // GET /profesores - Obtener todos los profesores
    @GetMapping("/profesores")
    public ResponseEntity<List<Profesor>> getAllProfesores() {
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }

    // GET /profesores/{id} - Obtener un profesor por ID
    @GetMapping("/profesores/{id}")
    public ResponseEntity<?> getProfesorById(@PathVariable int id) {
        Optional<Profesor> profesor = profesores.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (profesor.isPresent()) {
            return new ResponseEntity<>(profesor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // POST /profesores - Crear un nuevo profesor
    @PostMapping("/profesores")
    public ResponseEntity<?> createProfesor(@RequestBody Profesor nuevoProfesor) {
        // Validaciones básicas
        if (nuevoProfesor.getId() <= 0)
            return new ResponseEntity<>("El ID debe ser un número positivo", HttpStatus.BAD_REQUEST);

        if (nuevoProfesor.getNumeroEmpleado() <= 0)
            return new ResponseEntity<>("El número de empleado debe ser un número positivo", HttpStatus.BAD_REQUEST);

        if (isNullOrEmpty(nuevoProfesor.getNombres()) || isNullOrEmpty(nuevoProfesor.getApellidos()))
            return new ResponseEntity<>("Los nombres y apellidos no pueden estar vacíos", HttpStatus.BAD_REQUEST);

        if (nuevoProfesor.getHorasClase() < 0)
            return new ResponseEntity<>("Las horas de clase no pueden ser negativas", HttpStatus.BAD_REQUEST);

        // Verificar si ya existe un profesor con ese ID o número de empleado
        boolean exists = profesores.stream().anyMatch(p ->
                p.getId() == nuevoProfesor.getId() ||
                        p.getNumeroEmpleado() == nuevoProfesor.getNumeroEmpleado()
        );

        if (exists)
            return new ResponseEntity<>("Ya existe un profesor con ese ID o número de empleado", HttpStatus.BAD_REQUEST);

        profesores.add(nuevoProfesor);
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }

    // PUT /profesores/{id} - Actualizar un profesor existente
    @PutMapping("/profesores/{id}")
    public ResponseEntity<?> updateProfesor(@PathVariable int id, @RequestBody Profesor profesorActualizado) {
        Optional<Profesor> profesorOpt = profesores.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (!profesorOpt.isPresent()) {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
        }

        Profesor profesor = profesorOpt.get();

        // Validaciones
        if (isNullOrEmpty(profesorActualizado.getNombres()) || isNullOrEmpty(profesorActualizado.getApellidos()))
            return new ResponseEntity<>("Los nombres y apellidos no pueden estar vacíos", HttpStatus.BAD_REQUEST);

        if (profesorActualizado.getNumeroEmpleado() <= 0)
            return new ResponseEntity<>("El número de empleado debe ser positivo", HttpStatus.BAD_REQUEST);

        if (profesorActualizado.getHorasClase() < 0)
            return new ResponseEntity<>("Las horas de clase no pueden ser negativas", HttpStatus.BAD_REQUEST);

        // Actualizar datos
        profesor.setNombres(profesorActualizado.getNombres());
        profesor.setApellidos(profesorActualizado.getApellidos());
        profesor.setNumeroEmpleado(profesorActualizado.getNumeroEmpleado());
        profesor.setHorasClase(profesorActualizado.getHorasClase());

        return new ResponseEntity<>(profesor, HttpStatus.OK);
    }

    // DELETE /profesores/{id} - Eliminar un profesor
    @DeleteMapping("/profesores/{id}")
    public ResponseEntity<?> deleteProfesor(@PathVariable int id) {
        Optional<Profesor> profesorOpt = profesores.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (!profesorOpt.isPresent()) {
            return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
        }

        profesores.remove(profesorOpt.get());
        return new ResponseEntity<>("Profesor eliminado correctamente", HttpStatus.OK);
    }

    // Método auxiliar para validar texto vacío
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}