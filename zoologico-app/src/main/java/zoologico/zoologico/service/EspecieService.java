package zoologico.zoologico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoologico.zoologico.model.Especie;
import zoologico.zoologico.repository.EspecieRepository;
import java.util.List;
import java.util.Optional;

@Service
public class EspecieService {

    @Autowired
    private EspecieRepository especieRepository;

    public List<Especie> listarEspecies() {
        return especieRepository.findByActivoTrue();
    }

    public Optional<Especie> buscarPorId(Long id) {
        return especieRepository.findById(id);
    }

    public Optional<Especie> buscarPorNombreCientifico(String nombreCientifico) {
        return especieRepository.findByNombreCientifico(nombreCientifico);
    }

    public Optional<Especie> buscarPorNombre(String nombre) {
        return especieRepository.findByNombre(nombre);
    }

    public void guardar(Especie especie) {
        especieRepository.save(especie);
    }

    public void eliminar(Especie especie) {
        /*especie.setActivo(false);
        especieRepository.save(especie);*/
        especieRepository.delete(especie);
    }
}