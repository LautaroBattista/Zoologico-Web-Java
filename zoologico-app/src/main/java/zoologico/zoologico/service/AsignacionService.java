package zoologico.zoologico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoologico.zoologico.model.Asignacion;
import zoologico.zoologico.repository.AsignacionRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AsignacionService {

    @Autowired
    private AsignacionRepository asignacionRepository;

    public List<Asignacion> listarAsignaciones() {
        return asignacionRepository.findAll();
    }

    public Optional<Asignacion> buscarPorId(Long id) {
        return asignacionRepository.findById(id);
    }

    public void guardar(Asignacion asignacion) {
        asignacionRepository.save(asignacion);
    }

    public void eliminar(Asignacion asignacion) {
        asignacionRepository.delete(asignacion);
    }
}