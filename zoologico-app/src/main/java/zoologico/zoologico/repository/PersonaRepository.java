package zoologico.zoologico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoologico.zoologico.model.Persona;
import java.util.Optional;
import java.util.List;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    // Un solo resultado o ninguno -> Optional
    Optional<Persona> findByUsuarioAndPassword(String usuario, String password);
    Optional<Persona> findByUsuario(String usuario);
    // Puede ser muchos o ninguno -> List
    List<Persona> findByActivoTrue();
}
