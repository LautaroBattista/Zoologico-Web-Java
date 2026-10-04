package zoologico.zoologico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoologico.zoologico.model.Zona;
import java.util.Optional;
import java.util.List;

public interface ZonaRepository extends JpaRepository<Zona, Long> {
    Optional<Zona> findByNombre(String nombre);
    List<Zona> findByActivoTrue();
}