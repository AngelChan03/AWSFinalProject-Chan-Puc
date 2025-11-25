import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class AlumnosApiTest {

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
                .get("/alumnosinvaidpath")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void testUnsuportedMethod() {
        given().spec(SPEC)
                .log().all()
                .delete("/alumnos")
                .then()
                .log().all()
                .statusCode(405);
    }

    @Test
    public void testGetAlumnos() {
        given().spec(SPEC)
                .log().all()
                .get("/alumnos")
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON);
    }

    @Test
    public void testPostAlumno() {

        Map<String, Object> alumno = getAlumno();

        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .post("/alumnos")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);
    }

    @Test
    public void testGetAlumnoById() {

        Map<String, Object> alumno = getAlumno();

        System.out.println("=== Creando alumno ===");
        System.out.println("Datos esperados: " + alumno);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .post("/alumnos")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        System.out.println("\n=== Obteniendo alumno por ID ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .get("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON)
                .body("nombres", equalTo(alumno.get("nombres")))
                .body("matricula", equalTo(alumno.get("matricula")));
    }

    @Test
    public void testPutAlumno() {

        Map<String, Object> alumno = getAlumno();

        System.out.println("=== Creando alumno ===");
        System.out.println("Datos iniciales: " + alumno);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .post("/alumnos")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        System.out.println("\n=== Verificando alumno creado ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .get("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON)
                .body("nombres", equalTo(alumno.get("nombres")))
                .body("matricula", equalTo(alumno.get("matricula")));

        alumno.put("nombres", "Nuevo Eduardo");
        alumno.put("matricula", "A" + Constants.getRandomId());

        System.out.println("\n=== Actualizando alumno ===");
        System.out.println("Datos actualizados: " + alumno);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .put("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON);

        System.out.println("\n=== Verificando actualización ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .get("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON)
                .body("nombres", equalTo(alumno.get("nombres")))
                .body("matricula", equalTo(alumno.get("matricula")));

    }

    @Test
    public void testPutAlumnoWithWrongFields() {

        Map<String, Object> alumno = getAlumno();

        System.out.println("=== Creando alumno ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .post("/alumnos")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .get("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON)
                .body("nombres", equalTo(alumno.get("nombres")))
                .body("matricula", equalTo(alumno.get("matricula")));

        alumno.put("nombres", null);
        alumno.put("matricula", -1.223d);

        System.out.println("\n=== Intentando actualizar con campos inválidos ===");
        System.out.println("Datos inválidos enviados: " + alumno);
        System.out.println("Se espera: Status 400 (Bad Request)");
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .put("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(400).contentType(ContentType.JSON);
    }

    @Test
    public void testPostAlumnoWithWrongFields() {

        Map<String, Object> alumno = new HashMap<>();
        alumno.put("id", 0);
        alumno.put("nombres", "");
        alumno.put("apellidos", null);
        alumno.put("matricula", Constants.getRandomId());
        alumno.put("promedio", -1.2d);

        System.out.println("=== Intentando crear alumno con campos inválidos ===");
        System.out.println("Datos inválidos enviados: " + alumno);
        System.out.println("Se espera: Status 400 (Bad Request)");
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .post("/alumnos")
                .then()
                .log().all()
                .statusCode(400).contentType(ContentType.JSON);
    }

    @Test
    public void testDeleteAlumno() {

        Map<String, Object> alumno = getAlumno();

        System.out.println("=== Creando alumno para eliminar ===");
        System.out.println("Datos: " + alumno);
        
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .post("/alumnos")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        System.out.println("\n=== Verificando que existe ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .get("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(200).contentType(ContentType.JSON);

        System.out.println("\n=== Eliminando alumno ===");
        given().spec(SPEC)
                .log().all()
                .delete("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("\n=== Verificando que ya no existe ===");
        System.out.println("Se espera: Status 404 (Not Found)");
        given().spec(SPEC)
                .log().all()
                .get("/alumnos/" + alumno.get("id"))
                .then()
                .log().all()
                .statusCode(404);

    }

    @Test
    public void testDeleteWrongAlumno() {

        Map<String, Object> alumno = getAlumno();

        System.out.println("=== Creando alumno ===");
        given().spec(SPEC)
                .log().all()
                .contentType(ContentType.JSON)
                .body(alumno)
                .post("/alumnos")
                .then()
                .log().all()
                .statusCode(201).contentType(ContentType.JSON);

        int randomId = Constants.getRandomId();
        System.out.println("\n=== Intentando eliminar alumno inexistente ===");
        System.out.println("ID inexistente: " + randomId);
        System.out.println("Se espera: Status 404 (Not Found)");
        
        given().spec(SPEC)
                .log().all()
                .delete("/alumnos/" + randomId)
                .then()
                .log().all()
                .statusCode(404);

    }

    private Map<String, Object> getAlumno() {
        Map<String, Object> alumno = new HashMap<>();
        alumno.put("id", Constants.getRandomId());
        alumno.put("nombres", "Eduardo");
        alumno.put("apellidos", "Rodriguez");
        alumno.put("matricula", "A" + Constants.getRandomId());
        alumno.put("promedio", Constants.getPromedio());
        return alumno;
    }

}