package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter//Lombok genera los getters automaticamente
@Setter//Lombok genera los setters automaticamente
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Inheritance(strategy = InheritanceType.JOINED)//Cada subclase tiene su propia tabla, para herencia
@Table(name = "personas") //El nombre de la tabla en MySQL
public abstract class Persona {

    @Id//clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id se genera automaticamente
    private Long id;

    @Column(unique = true, nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean activo = true;

    public Persona() {}

    public Persona(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }
}
