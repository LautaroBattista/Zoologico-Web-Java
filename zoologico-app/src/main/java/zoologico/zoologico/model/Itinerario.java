package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Table(name = "itinerarios") //El nombre de la tabla en MySQL
public class Itinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int codigo;

    @Column(nullable = false)
    private int duracionRecorrido;

    @Column(nullable = false)
    private double longitud;

    @Column(nullable = false)
    private int maxVisitantes;

    /*@ManyToMany(mappedBy = "itinerarios") // "itinerarios" debe ser el nombre del atributo en la clase Guia
    private List<Guia> guias = new ArrayList<>();*/

    @ManyToMany
    @JoinTable(//no molesta el delete porque itinerario es el lado dueño
        name = "itinerario_zona",
        joinColumns = @JoinColumn(name = "itinerario_id"),
        inverseJoinColumns = @JoinColumn(name = "zona_id")
    )
    private List<Zona> zonas = new ArrayList<>();

    @OneToMany(mappedBy = "itinerario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Asignacion> asignaciones = new ArrayList<>();

    @Column(nullable = false)
    private boolean activo = true;

    public Itinerario() {}

    public Itinerario(int codigo, int duracionRecorrido, double longitud, int maxVisitantes) {
        this.codigo = codigo;
        this.duracionRecorrido = duracionRecorrido;
        this.longitud = longitud;
        this.maxVisitantes = maxVisitantes;
    }

    // Calculado — no se guarda en BD
    public int getCantEspecies() {
        Set<Especie> especiesUnicas = new HashSet<>();
        for (Zona zona : zonas) {
            especiesUnicas.addAll(zona.getEspecies());
        }
        return especiesUnicas.size();
    }

    /*public void agregarGuia(Guia g) { guias.add(g); }
    public void eliminarGuia(Guia g) { guias.remove(g); }*/

    public void agregarZona(Zona z) { zonas.add(z); }
    public void eliminarZona(Zona z) { zonas.remove(z); }

    public void agregarAsignacion(Asignacion a) { asignaciones.add(a); }
    public void eliminarAsignacion(Asignacion a) { asignaciones.remove(a); }
}