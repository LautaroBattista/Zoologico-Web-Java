package zoologico.zoologico.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zoologico.zoologico.model.Asignacion;
import zoologico.zoologico.model.Guia;
import zoologico.zoologico.model.Itinerario;
import zoologico.zoologico.model.Persona;
import zoologico.zoologico.service.*;

@Controller
@RequestMapping("/guia")
public class GuiaController {

    @Autowired
    private PersonaService personaService;
    /*@Autowired
    private ItinerarioService itinerarioService;*/

    // INICIO
    @GetMapping("/inicio")
    public String inicio() {
        return "guia/inicio";
    }

    // VER ITINERARIOS
    @GetMapping("/itinerarios")
    public String verItinerarios(Model model, Principal principal) {
        //System.out.println("Usuario logueado: " + principal.getName());
        Optional<Persona> opt = personaService.buscarPorUsuario(principal.getName());
        //System.out.println("Persona encontrada: " + opt.isPresent());
        /*if (opt.isPresent()) {
            System.out.println("Tipo: " + opt.get().getClass().getSimpleName());
        }
        Persona persona = opt.get();
        Guia guia = (Guia) persona;
        model.addAttribute("itinerarios", guia.getItinerarios());*/
        if (opt.isPresent()) {
            Guia guia = (Guia) opt.get();

            List<Itinerario> itinerariosActivos = guia.getAsignaciones()
                    .stream()
                    .map(Asignacion::getItinerario)// De cada asignacion sacas el itinerario
                    .filter(Itinerario::isActivo)
                    .distinct()
                    .toList();

            model.addAttribute("itinerarios", itinerariosActivos);
        }
        return "guia/itinerarios";
    }
}