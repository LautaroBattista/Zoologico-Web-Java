package zoologico.zoologico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zoologico.zoologico.service.PersonaService;

@Controller
public class HomeController {

    @Autowired
    private PersonaService personaService;

    // GET / — si no hay usuarios, va a setup; si ya hay, va a login
    @GetMapping("/")
    public String home() {
        return personaService.listarPersonas().isEmpty() ? "redirect:/setup" : "redirect:/login";
    }
}