package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Table(name = "guias") //El nombre de la tabla en MySQL
public class Guia extends Empleado {

    @Column(nullable = false)
    private String fechaDeInicioDeTrabajoAlZoo;

    /*@ManyToMany
    @JoinTable(
        name = "guia_itinerario",
        joinColumns = @JoinColumn(name = "guia_id"),
        inverseJoinColumns = @JoinColumn(name = "itinerario_id")
    )
    private List<Itinerario> itinerarios = new ArrayList<>();*/

    @OneToMany(mappedBy = "guia")
    private List<Asignacion> asignaciones = new ArrayList<>();

    public Guia() {}

    public Guia(String usuario, String password, String nombre, String direccion, String telefono, String fechaDeInicioDeTrabajoAlZoo) {
        super(usuario, password, nombre, direccion, telefono);
        this.fechaDeInicioDeTrabajoAlZoo = fechaDeInicioDeTrabajoAlZoo;
    }

    /*public void agregarItinerario(Itinerario i){ itinerarios.add(i); }
    public void eliminarItinerario(Itinerario i){ itinerarios.remove(i); }*/

    public void agregarAsignacion(Asignacion a) { asignaciones.add(a); }
    public void eliminarAsignacion(Asignacion a) { asignaciones.remove(a); }
}
