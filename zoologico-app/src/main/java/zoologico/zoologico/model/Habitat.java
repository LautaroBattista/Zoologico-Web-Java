package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Table(name = "habitats") //El nombre de la tabla en MySQL
public class Habitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String clima;

    @Column(nullable = false)
    private String vegetacion;

    @ManyToMany(mappedBy = "habitats") // "habitats" debe ser el nombre del atributo en la clase Especie
    private List<Especie> especies = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "habitat_continente",
        joinColumns = @JoinColumn(name = "habitat_id")
    )
    @Column(name = "continente")
    @Enumerated(EnumType.STRING)
    private List<Continente> continentes = new ArrayList<>();

    @Column(nullable = false)
    private boolean activo = true;

    public Habitat() {}

    public Habitat(String nombre, String clima, String vegetacion) {
        this.nombre = nombre;
        this.clima = clima;
        this.vegetacion = vegetacion;
    }

    public void agregarEspecie(Especie e) { especies.add(e); }
    public void eliminarEspecie(Especie e) { especies.remove(e); }

    public void agregarContinente(Continente c) { if (!continentes.contains(c)) continentes.add(c); }
    public void eliminarContinente(Continente c) { continentes.remove(c); }
}