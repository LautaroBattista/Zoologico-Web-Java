package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Table(name = "cuidadores") //El nombre de la tabla en MySQL
public class Cuidador extends Empleado {

    @Column(nullable = false)
    private String fechaDeIngresoEnElParque;

    @OneToMany(mappedBy = "cuidador")
    private List<Cuidado> cuidados = new ArrayList<>();

    public Cuidador() {}

    public Cuidador(String usuario, String password, String nombre, String direccion, String telefono, String fechaDeIngresoEnElParque) {
        super(usuario, password, nombre, direccion, telefono);
        this.fechaDeIngresoEnElParque = fechaDeIngresoEnElParque;
    }

    public void agregarEspecie(Cuidado c){ cuidados.add(c); }
    public void eliminarEspecie(Cuidado c){ cuidados.remove(c); }
}
