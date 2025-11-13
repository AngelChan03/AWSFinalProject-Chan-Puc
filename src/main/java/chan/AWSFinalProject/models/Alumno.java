package chan.AWSFinalProject.models;

public class Alumno {
    private int id;
    private String nombres;
    private String apellidos;
    private int horasClase;

    public Alumno(String nombres, int id, String apellidos, int horasClase) {
        this.nombres = nombres;
        this.id = id;
        this.apellidos = apellidos;
        this.horasClase = horasClase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public int getHorasClase() {
        return horasClase;
    }

    public void setHorasClase(int horasClase) {
        this.horasClase = horasClase;
    }
}
