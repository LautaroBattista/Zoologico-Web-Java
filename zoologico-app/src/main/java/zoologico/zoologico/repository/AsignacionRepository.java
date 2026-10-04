package zoologico.zoologico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoologico.zoologico.model.Asignacion;
//import java.util.Optional;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    //Optional<Asignacion> findById(long id);
}