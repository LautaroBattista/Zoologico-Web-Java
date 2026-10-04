package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Table(name = "zonas") //El nombre de la tabla en MySQL
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double extension;

    @ManyToMany(mappedBy = "zonas") // "zonas" debe ser el nombre del atributo en la clase Itinerario
    private List<Itinerario> itinerarios = new ArrayList<>();

    @OneToMany(mappedBy = "zona")
    private List<Especie> especies = new ArrayList<>();

    @Column(nullable = false)
    private boolean activo = true;

    public Zona() {}

    public Zona(String nombre, Double extension) {
        this.nombre = nombre;
        this.extension = extension;
    }

    public void setId(Long id) { this.id = id; }

    public void agregarItinerario(Itinerario i) { itinerarios.add(i); }
    public void eliminarItinerario(Itinerario i) { itinerarios.remove(i); }

    public void agregarEspecie(Especie e) { especies.add(e); }
    public void eliminarEspecie(Especie e) { especies.remove(e); }
}