package zoologico.zoologico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoologico.zoologico.model.Especie;
import java.util.Optional;
import java.util.List;

public interface EspecieRepository extends JpaRepository<Especie, Long> {
    Optional<Especie> findByNombreCientifico(String nombreCientifico);
    Optional<Especie> findByNombre(String nombre);
    List<Especie> findByActivoTrue();
}
