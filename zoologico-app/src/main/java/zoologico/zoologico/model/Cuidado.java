package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cuidados")
public class Cuidado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuidador_id", nullable = false)
    private Cuidador cuidador;

    @ManyToOne
    @JoinColumn(name = "especie_id", nullable = false)
    private Especie especie;

    @Column(nullable = false)
    private String fechaIngreso;

    public Cuidado() {}

    public Cuidado(Cuidador cuidador, Especie especie, String fechaIngreso) {
        this.cuidador = cuidador;
        this.especie = especie;
        this.fechaIngreso = fechaIngreso;
    }
}