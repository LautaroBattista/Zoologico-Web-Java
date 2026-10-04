package zoologico.zoologico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoologico.zoologico.model.Habitat;
import java.util.Optional;
import java.util.List;

public interface HabitatRepository extends JpaRepository<Habitat, Long> {
    Optional<Habitat> findByNombre(String nombre);
    List<Habitat> findByActivoTrue();
}