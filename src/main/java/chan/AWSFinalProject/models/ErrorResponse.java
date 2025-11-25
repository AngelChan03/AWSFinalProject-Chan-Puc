package chan.AWSFinalProject.models;

public class ErrorResponse {
    private String mensaje;
    private int status;

    public ErrorResponse(String mensaje, int status) {
        this.mensaje = mensaje;
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}