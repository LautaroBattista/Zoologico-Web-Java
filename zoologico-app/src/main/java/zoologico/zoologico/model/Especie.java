package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Table(name = "especies") //El nombre de la tabla en MySQL
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String nombreCientifico;

    @Column(nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "zona_id")
    private Zona zona;

    @ManyToMany
    @JoinTable(
        name = "especie_habitat",
        joinColumns = @JoinColumn(name = "especie_id"),
        inverseJoinColumns = @JoinColumn(name = "habitat_id")
    )
    private List<Habitat> habitats = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Cuidado> cuidados = new ArrayList<>();

    @Column(nullable = false)
    private boolean activo = true;

    public Especie() {}

    public Especie(String nombre, String nombreCientifico, Zona zona, String descripcion) {
        this.nombre = nombre;
        this.nombreCientifico = nombreCientifico;
        this.zona = zona;
        this.descripcion = descripcion;
    }

    public void agregarCuidador(Cuidado c){ cuidados.add(c); }
    public void eliminarCuidador(Cuidado c){ cuidados.remove(c); }

    public void agregarHabitat(Habitat h){ habitats.add(h); }
    public void eliminarHabitat(Habitat h){ habitats.remove(h); }
}
