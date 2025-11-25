import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ProfesoresApiTest {

    private static String URL;
    private static RequestSpecification SPEC;

    @BeforeAll
    public static void setUrl() {
        URL = Constants.URL;
        SPEC = new RequestSpecBuilder().setBaseUri(URL).build();
    }

    @Test
    public void testInvalidPath() {
        given().spec(SPEC)
                .log().all()
                .get("/profesoresinvaidpath")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void testUnsuportedMethod() {
        given().spec(SPEC)
                .log().all()
                .delete("/profesores")
                .then()
                .log().all()
                .statusCode(405);
    }

    @Test
    public void testGetProfesores() {
        given().spec(SPEC)
                .log().all()
                .get("/profesores")
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON);
    }

    @Test
    public void testPostProfesor() {

        Map<String, Object> profesor = getProfesor();

        System.out.println("=== Creando profesor ===");
        System.out.println("Datos enviados: " + profesor);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .post("/profesores")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);
    }

    @Test
    public void testGetProfesorById() {

        Map<String, Object> profesor = getProfesor();

        System.out.println("=== Creando profesor ===");
        System.out.println("Datos esperados: " + profesor);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .post("/profesores")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        System.out.println("\n=== Obteniendo profesor por ID ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .get("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON)
                .body("nombres", equalTo(profesor.get("nombres")))
                .body("horasClase", equalTo(profesor.get("horasClase")));
    }

    @Test
    public void testPutProfesor() {

        Map<String, Object> profesor = getProfesor();

        System.out.println("=== Creando profesor ===");
        System.out.println("Datos iniciales: " + profesor);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .post("/profesores")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        System.out.println("\n=== Verificando profesor creado ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .get("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON)
                .body("nombres", equalTo(profesor.get("nombres")))
                .body("horasClase", equalTo(profesor.get("horasClase")));

        profesor.put("nombres", "Nuevo Profesor");
        profesor.put("horasClase", Constants.getRandomHoras());

        System.out.println("\n=== Actualizando profesor ===");
        System.out.println("Datos actualizados: " + profesor);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .put("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON);

        System.out.println("\n=== Verificando actualización ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .get("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON)
                .body("nombres", equalTo(profesor.get("nombres")))
                .body("horasClase", equalTo(profesor.get("horasClase")));

    }

    @Test
    public void testPutProfesorWithWrongFields() {

        Map<String, Object> profesor = getProfesor();

        System.out.println("=== Creando profesor ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .post("/profesores")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .get("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON)
                .body("nombres", equalTo(profesor.get("nombres")))
                .body("horasClase", equalTo(profesor.get("horasClase")));

        profesor.put("nombres", null);
        profesor.put("horasClase", -1.26d);

        System.out.println("\n=== Intentando actualizar con campos inválidos ===");
        System.out.println("Datos inválidos enviados: " + profesor);
        System.out.println("Se espera: Status 400 (Bad Request)");
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .put("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(400).contentType(ContentType.JSON);

    }

    @Test
    public void testPostProfesorWithWrongFields() {

        Map<String, Object> profesor = new HashMap<>();
        profesor.put("id", 0);
        profesor.put("nombres", "");
        profesor.put("apellidos", null);
        profesor.put("numeroEmpleado", -3688);
        profesor.put("horasClase", -1.26d);

        System.out.println("=== Intentando crear profesor con campos inválidos ===");
        System.out.println("Datos inválidos enviados: " + profesor);
        System.out.println("Se espera: Status 400 (Bad Request)");
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .post("/profesores")
                .then()
                .log().all()
                .statusCode(400).contentType(ContentType.JSON);
    }

    @Test
    public void testDeleteProfesor() {

        Map<String, Object> profesor = getProfesor();

        System.out.println("=== Creando profesor para eliminar ===");
        System.out.println("Datos: " + profesor);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .post("/profesores")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        System.out.println("\n=== Verificando que existe ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .get("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON);

        System.out.println("\n=== Eliminando profesor ===");
        given().spec(SPEC)
                .log().all()
                .delete("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("\n=== Verificando que ya no existe ===");
        System.out.println("Se espera: Status 404 (Not Found)");
        given().spec(SPEC)
                .log().all()
                .get("/profesores/" + profesor.get("id"))
                .then()
                .log().all()
                .statusCode(404);

    }

    @Test
    public void testDeleteWrongProfesor() {

        Map<String, Object> profesor = getProfesor();

        System.out.println("=== Creando profesor ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(profesor)
                .post("/profesores")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        int randomId = Constants.getRandomId();
        System.out.println("\n=== Intentando eliminar profesor inexistente ===");
        System.out.println("ID inexistente: " + randomId);
        System.out.println("Se espera: Status 404 (Not Found)");
        
        given().spec(SPEC)
                .log().all()
                .delete("/profesores/" + randomId)
                .then()
                .log().all()
                .statusCode(404);
    }

    private Map<String, Object> getProfesor() {
        Map<String, Object> profesor = new HashMap<>();
        profesor.put("id", Constants.getRandomId());
        profesor.put("nombres", "Profesor");
        profesor.put("apellidos", "Rodriguez");
        profesor.put("numeroEmpleado", Constants.getRandomId());
        profesor.put("horasClase", Constants.getRandomHoras());
        return profesor;
    }

}