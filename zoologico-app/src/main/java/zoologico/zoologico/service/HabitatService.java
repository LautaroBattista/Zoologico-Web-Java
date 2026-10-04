package zoologico.zoologico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoologico.zoologico.model.Habitat;
import zoologico.zoologico.repository.HabitatRepository;
import java.util.List;
import java.util.Optional;

@Service
public class HabitatService {

    @Autowired
    private HabitatRepository habitatRepository;

    public List<Habitat> listarHabitats() {
        return habitatRepository.findByActivoTrue();
    }

    public Optional<Habitat> buscarPorId(Long id) {
        return habitatRepository.findById(id);
    }

    public Optional<Habitat> buscarPorNombre(String nombre) {
        return habitatRepository.findByNombre(nombre);
    }

    public void guardar(Habitat habitat) {
        habitatRepository.save(habitat);
    }

    public void eliminar(Habitat habitat) {
        habitat.setActivo(false);
        habitatRepository.save(habitat);
    }
}