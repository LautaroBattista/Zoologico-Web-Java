package zoologico.zoologico.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zoologico.zoologico.model.Cuidado;
import zoologico.zoologico.model.Cuidador;
import zoologico.zoologico.model.Persona;
import zoologico.zoologico.service.*;

@Controller
@RequestMapping("/cuidador")
public class CuidadorController {

    @Autowired
    private PersonaService personaService;
    /*@Autowired
    private EspecieService especieService;*/

    // INICIO
    @GetMapping("/inicio")
    public String inicio() {
        return "cuidador/inicio";
    }

    // VER ESPECIES
    @GetMapping("/especies")
    public String verEspecies(Model model, Principal principal) {
        Optional<Persona> opt = personaService.buscarPorUsuario(principal.getName());
        if (opt.isPresent()) {
            Cuidador cuidador = (Cuidador) opt.get();
            // Filtrar solo los cuidados cuya especie esté activa
            List<Cuidado> cuidadosActivos = cuidador.getCuidados()
                    .stream()
                    .filter(c -> c.getEspecie().isActivo())
                    .collect(Collectors.toList());
            model.addAttribute("cuidados", cuidadosActivos);
        }
        return "cuidador/especies";
    }
}