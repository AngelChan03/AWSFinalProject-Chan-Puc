package chan.AWSFinalProject.models;

public class Profesor {
    private int id;
    private int numeroEmpleado;
    private String nombres;
    private String apellidos;
    private int horasClase;

    // Constructor por defecto necesario para Spring/Jackson
    public Profesor() {
    }

    public Profesor(int id, String nombres, int numeroEmpleado, String apellidos, int horasClase) {
        this.id = id;
        this.nombres = nombres;
        this.numeroEmpleado = numeroEmpleado;
        this.apellidos = apellidos;
        this.horasClase = horasClase;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getHorasClase() {
        return horasClase;
    }

    public void setHorasClase(int horasClase) {
        this.horasClase = horasClase;
    }
}