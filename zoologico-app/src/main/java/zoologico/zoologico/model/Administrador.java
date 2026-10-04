package zoologico.zoologico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity //Le dice a Hibernate: esta clase se guarda en la BD
@Table(name = "administradores") //El nombre de la tabla en MySQL
public class Administrador extends Persona {

    public Administrador() {}

    public Administrador(String usuario, String password) {
        super(usuario, password);
    }
}

