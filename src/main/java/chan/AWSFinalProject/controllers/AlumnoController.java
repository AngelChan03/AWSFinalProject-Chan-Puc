package chan.AWSFinalProject.controllers;

import chan.AWSFinalProject.models.Alumno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AlumnoController {

    private List<Alumno> alumnos = new ArrayList<>();

    // ✅ GET /alumnos - obtener todos los alumnos
    @GetMapping("/alumnos")
    public ResponseEntity<List<Alumno>> getAllAlumnos() {
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    // ✅ GET /alumnos/{id} - obtener un alumno por ID
    @GetMapping("/alumnos/{id}")
    public ResponseEntity<?> getAlumnoById(@PathVariable int id) {
        Optional<Alumno> alumno = alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (alumno.isPresent()) {
            return new ResponseEntity<>(alumno.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // POST /alumnos - crear un alumno
    @PostMapping("/alumnos")
    public ResponseEntity<?> createAlumno(@RequestBody Alumno nuevoAlumno) {
        // Validaciones
        if (nuevoAlumno.getId() <= 0)
            return new ResponseEntity<>("El ID debe ser un número positivo", HttpStatus.BAD_REQUEST);
        if (isNullOrEmpty(nuevoAlumno.getNombres()) || isNullOrEmpty(nuevoAlumno.getApellidos()))
            return new ResponseEntity<>("Los nombres y apellidos no pueden estar vacíos", HttpStatus.BAD_REQUEST);
        if (nuevoAlumno.getHorasClase() < 0)
            return new ResponseEntity<>("Las horas de clase no pueden ser negativas", HttpStatus.BAD_REQUEST);

        // Validar si ya existe un alumno con el mismo ID
        boolean exists = alumnos.stream().anyMatch(a -> a.getId() == nuevoAlumno.getId());
        if (exists)
            return new ResponseEntity<>("Ya existe un alumno con ese ID", HttpStatus.BAD_REQUEST);

        alumnos.add(nuevoAlumno);
        System.out.println("Se creó un nuevo alumno");
        return new ResponseEntity<>(nuevoAlumno, HttpStatus.CREATED);

    }

    // PUT /alumnos/{id} - actualizar un alumno existente
    @PutMapping("/alumnos/{id}")
    public ResponseEntity<?> updateAlumno(@PathVariable int id, @RequestBody Alumno alumnoActualizado) {
        Optional<Alumno> alumnoOpt = alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (!alumnoOpt.isPresent()) {
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);
        }

        Alumno alumno = alumnoOpt.get();

        // Validaciones
        if (isNullOrEmpty(alumnoActualizado.getNombres()) || isNullOrEmpty(alumnoActualizado.getApellidos()))
            return new ResponseEntity<>("Los nombres y apellidos no pueden estar vacíos", HttpStatus.BAD_REQUEST);
        if (alumnoActualizado.getHorasClase() < 0)
            return new ResponseEntity<>("Las horas de clase no pueden ser negativas", HttpStatus.BAD_REQUEST);

        // Actualizar datos
        alumno.setNombres(alumnoActualizado.getNombres());
        alumno.setApellidos(alumnoActualizado.getApellidos());
        alumno.setHorasClase(alumnoActualizado.getHorasClase());

        return new ResponseEntity<>(alumno, HttpStatus.CREATED);
    }

    //  DELETE /alumnos/{id} - eliminar un alumno
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<?> deleteAlumno(@PathVariable int id) {
        Optional<Alumno> alumnoOpt = alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (!alumnoOpt.isPresent()) {
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);
        }

        alumnos.remove(alumnoOpt.get());
        return new ResponseEntity<>("Alumno eliminado correctamente", HttpStatus.OK);
    }

    // Método auxiliar para validar texto vacío
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
