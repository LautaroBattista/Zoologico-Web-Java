package zoologico.zoologico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoologico.zoologico.model.Zona;
import zoologico.zoologico.repository.ZonaRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ZonaService {

    @Autowired
    private ZonaRepository zonaRepository;

    public Optional<Zona> buscarPorId(Long id) {
        return zonaRepository.findById(id);
    }

    public List<Zona> listarZonas() {
        return zonaRepository.findByActivoTrue();
    }

    public Optional<Zona> buscarPorNombre(String nombre) {
        return zonaRepository.findByNombre(nombre);
    }

    public void guardar(Zona zona) {
        zonaRepository.save(zona);
    }

    public void eliminar(Zona zona) {
        zona.setActivo(false);
        zonaRepository.save(zona);
    }
}