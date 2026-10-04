package zoologico.zoologico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoologico.zoologico.model.Itinerario;
import zoologico.zoologico.repository.ItinerarioRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ItinerarioService {

    @Autowired
    private ItinerarioRepository itinerarioRepository;

    public List<Itinerario> listarItinerarios() {
        return itinerarioRepository.findByActivoTrue();
    }

    public Optional<Itinerario> buscarPorId(Long id) {
        return itinerarioRepository.findById(id);
    }

    public Optional<Itinerario> buscarPorNombre(int codigo) {
        return itinerarioRepository.findByCodigo(codigo);
    }

    public void guardar(Itinerario itinerario) {
        itinerarioRepository.save(itinerario);
    }

    public void eliminar(Itinerario itinerario) {
        /*itinerario.setActivo(false);
        itinerarioRepository.save(itinerario);*/
        itinerarioRepository.delete(itinerario);
    }
}