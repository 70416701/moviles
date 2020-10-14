package org.EvalPar.torres.torres_evalparcial.ui.ui.entidad;

public class Usuarios {

    private Integer id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String pagina;

    public Usuarios(Integer id, String nombre, String apellido, String telefono, String email, String pagina) {
        this.setId(id);
        this.setNombre(nombre);
        this.setApellido(apellido);
        this.setTelefono(telefono);
        this.setEmail(email);
        this.setPagina(pagina);
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

}
