package zoologico.zoologico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoologico.zoologico.model.Cuidado;
import zoologico.zoologico.repository.CuidadoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CuidadoService {

    @Autowired
    private CuidadoRepository cuidadoRepository;

    public List<Cuidado> listarCuidados() {
        return cuidadoRepository.findAll();
    }

    public Optional<Cuidado> buscarPorId(Long id) {
        return cuidadoRepository.findById(id);
    }

    public void guardar(Cuidado cuidado) {
        cuidadoRepository.save(cuidado);
    }

    public void eliminar(Cuidado cuidado) {
        cuidadoRepository.delete(cuidado);
    }
}