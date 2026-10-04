package zoologico.zoologico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zoologico.zoologico.model.Administrador;
import zoologico.zoologico.service.PersonaService;

@Controller
public class SetupController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // GET /setup — muestra el formulario para crear el primer admin
    @GetMapping("/setup")
    public String mostrarSetup() {
        return personaService.listarPersonas().isEmpty() ? "setup" : "redirect:/login";
    }

    // POST /setup — recibe los datos del formulario y crea el admin
    @PostMapping("/setup")
    public String crearAdmin(@RequestParam String usuario,
                             @RequestParam String password) {
        if (personaService.listarPersonas().isEmpty()) {
            // Crea el admin con la password encriptada
            personaService.guardar(new Administrador(usuario, passwordEncoder.encode(password)));
        }
        return "redirect:/login";
    }

}