package chan.AWSFinalProject.controllers;

import chan.AWSFinalProject.models.Alumno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alumnos") // <--- ¡AQUÍ ESTÁ EL TRUCO!
public class AlumnoController {

    private List<Alumno> alumnos = new ArrayList<>();

    // Esto ahora genera la ruta: /alumnos/alumnos
    @GetMapping("/alumnos")
    public ResponseEntity<List<Alumno>> getAllAlumnos() {
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    // Esto ahora genera la ruta: /alumnos/alumnos/{id}
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

    // Esto ahora genera la ruta: /alumnos/alumnos
    @PostMapping("/alumnos")
    public ResponseEntity<?> createAlumno(@RequestBody Alumno nuevoAlumno) {
        // Validaciones
        if (nuevoAlumno.getId() <= 0)
            return new ResponseEntity<>("El ID debe ser un número positivo", HttpStatus.BAD_REQUEST);

        if (isNullOrEmpty(nuevoAlumno.getNombres()) || isNullOrEmpty(nuevoAlumno.getApellidos()))
            return new ResponseEntity<>("Los nombres y apellidos no pueden estar vacíos", HttpStatus.BAD_REQUEST);

        if (isNullOrEmpty(nuevoAlumno.getMatricula()))
            return new ResponseEntity<>("La matrícula no puede estar vacía", HttpStatus.BAD_REQUEST);

        if (nuevoAlumno.getPromedio() < 0 || nuevoAlumno.getPromedio() > 1)
            return new ResponseEntity<>("El promedio debe estar entre 0 y 1", HttpStatus.BAD_REQUEST);

        boolean exists = alumnos.stream().anyMatch(a -> a.getId() == nuevoAlumno.getId());
        if (exists)
            return new ResponseEntity<>("Ya existe un alumno con ese ID", HttpStatus.BAD_REQUEST);

        alumnos.add(nuevoAlumno);
        return new ResponseEntity<>(nuevoAlumno, HttpStatus.CREATED);
    }

    // Esto ahora genera la ruta: /alumnos/alumnos/{id}
    @PutMapping("/alumnos/{id}")
    public ResponseEntity<?> updateAlumno(@PathVariable int id, @RequestBody Alumno alumnoActualizado) {
        Optional<Alumno> alumnoOpt = alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (!alumnoOpt.isPresent())
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);

        Alumno alumno = alumnoOpt.get();

        if (isNullOrEmpty(alumnoActualizado.getNombres()) || isNullOrEmpty(alumnoActualizado.getApellidos()))
            return new ResponseEntity<>("Los nombres y apellidos no pueden estar vacíos", HttpStatus.BAD_REQUEST);

        if (isNullOrEmpty(alumnoActualizado.getMatricula()))
            return new ResponseEntity<>("La matrícula no puede estar vacía", HttpStatus.BAD_REQUEST);

        if (alumnoActualizado.getPromedio() < 0 || alumnoActualizado.getPromedio() > 1)
            return new ResponseEntity<>("El promedio debe estar entre 0 y 1", HttpStatus.BAD_REQUEST);

        alumno.setNombres(alumnoActualizado.getNombres());
        alumno.setApellidos(alumnoActualizado.getApellidos());
        alumno.setMatricula(alumnoActualizado.getMatricula());
        alumno.setPromedio(alumnoActualizado.getPromedio());

        return new ResponseEntity<>(alumno, HttpStatus.OK);
    }

    // Esto ahora genera la ruta: /alumnos/alumnos/{id}
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<?> deleteAlumno(@PathVariable int id) {
        Optional<Alumno> alumnoOpt = alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();

        if (!alumnoOpt.isPresent())
            return new ResponseEntity<>("Alumno no encontrado", HttpStatus.NOT_FOUND);

        alumnos.remove(alumnoOpt.get());
        return new ResponseEntity<>("Alumno eliminado correctamente", HttpStatus.OK);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}