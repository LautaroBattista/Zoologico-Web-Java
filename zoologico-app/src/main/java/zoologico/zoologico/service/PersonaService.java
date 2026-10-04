package zoologico.zoologico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zoologico.zoologico.model.Administrador;
import zoologico.zoologico.model.Cuidador;
import zoologico.zoologico.model.Guia;
import zoologico.zoologico.model.Persona;
import zoologico.zoologico.repository.PersonaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Le dice a Spring que esta clase es un servicio
@Service
public class PersonaService implements UserDetailsService {
    // Le dice a Spring que inyecte automáticamente el repositorio. No necesitás hacer new PersonaRepository()
    @Autowired
    private PersonaRepository personaRepository;

    // METODO DE UserDetailsService — Spring lo llama cuando alguien se loguea
    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        Persona persona = personaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usuario));

        // Verificar que esté activo
        if (!persona.isActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        String rol;
        if (persona instanceof Administrador) rol = "ROLE_ADMINISTRADOR";
        else if (persona instanceof Guia) rol = "ROLE_GUIA";
        else if (persona instanceof Cuidador) rol = "ROLE_CUIDADOR";
        else rol = "ROLE_USER";

        return new User(
                persona.getUsuario(),
                persona.getPassword(),
                List.of(new SimpleGrantedAuthority(rol))
        );
    }

    public List<Persona> listarPersonas() {
        return personaRepository.findByActivoTrue();
    }

    public List<Cuidador> listarCuidadores() {
        return personaRepository.findByActivoTrue()
            .stream() // transforma la lista en un stream, permite aplicar operaciones una tras otra.
            .filter(p -> p instanceof Cuidador)
            .map(p -> (Cuidador) p) // las transforma en cuidador 
            .collect(Collectors.toList()); // Termina el stream y lo convierte de vuelta en una List
    }

    public List<Guia> listarGuias() {
        return personaRepository.findByActivoTrue()
            .stream() // El stream no es una lista, es una cadena de operaciones.
            .filter(p -> p instanceof Guia)
            .map(p -> (Guia) p)
            .collect(Collectors.toList()); // El collect la materializa en una lista real.
    }

    public Optional<Persona> buscarPorId(Long id) {
        return personaRepository.findById(id);
    }

    public Optional<Persona> buscarPorUsuarioYPassword(String usuario, String password) {
        return personaRepository.findByUsuarioAndPassword(usuario, password);
    }

    public Optional<Persona> buscarPorUsuario(String usuario) {
        return personaRepository.findByUsuario(usuario);
    }

    public void guardar(Persona persona) {
        personaRepository.save(persona);
    }

    public void eliminar(Persona persona) {
        persona.setActivo(false);
        personaRepository.save(persona);
    }
}