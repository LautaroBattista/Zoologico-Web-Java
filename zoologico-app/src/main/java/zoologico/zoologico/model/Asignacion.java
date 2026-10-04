package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "asignaciones")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guia_id", nullable = false)
    private Guia guia;

    @ManyToOne
    @JoinColumn(name = "itinerario_id", nullable = false)
    private Itinerario itinerario;

    @Column(nullable = false)
    private String hora;

    public Asignacion() {}

    public Asignacion(Guia guia, Itinerario itinerario, String hora) {
        this.guia = guia;
        this.itinerario = itinerario;
        this.hora = hora;
    }
}