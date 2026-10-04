package zoologico.zoologico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoologico.zoologico.model.Cuidado;
//import java.util.Optional;

public interface CuidadoRepository extends JpaRepository<Cuidado, Long> {}
