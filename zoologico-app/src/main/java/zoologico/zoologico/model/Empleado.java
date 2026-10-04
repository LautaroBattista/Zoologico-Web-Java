package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Table(name = "empleados") //El nombre de la tabla en MySQL
public abstract class Empleado extends Persona {

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String telefono;

    public Empleado() {}

    public Empleado(String usuario, String password, String nombre, String direccion, String telefono) {
        super(usuario, password);
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }
}
