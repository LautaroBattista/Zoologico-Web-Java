package zoologico.zoologico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoologico.zoologico.model.Itinerario;
import java.util.Optional;
import java.util.List;

public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {
    Optional<Itinerario> findByCodigo(int codigo);
    List<Itinerario> findByActivoTrue();
}
