package zoologico.zoologico.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zoologico.zoologico.model.Asignacion;
import zoologico.zoologico.model.Continente;
import zoologico.zoologico.model.Cuidado;
import zoologico.zoologico.model.Cuidador;
import zoologico.zoologico.model.Especie;
import zoologico.zoologico.model.Guia;
import zoologico.zoologico.model.Habitat;
import zoologico.zoologico.model.Itinerario;
import zoologico.zoologico.model.Persona;
import zoologico.zoologico.model.Zona;
import zoologico.zoologico.service.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PersonaService personaService;
    @Autowired
    private EspecieService especieService;
    @Autowired
    private HabitatService habitatService;
    @Autowired
    private ZonaService zonaService;
    @Autowired
    private ItinerarioService itinerarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CuidadoService cuidadoService;
    @Autowired
    private AsignacionService asignacionService;

    // INICIO
    @GetMapping("/inicio")
    public String inicio() {
        return "admin/inicio";
    }
    // **** VER ****
    // VER PERSONAS
    @GetMapping("/personas")
    public String verPersonas(Model model) {
        model.addAttribute("personas", personaService.listarPersonas());
        return "admin/personas";
    }

    // VER ESPECIES
    @GetMapping("/especies")
    public String verEspecies(Model model) {
        model.addAttribute("especies", especieService.listarEspecies());
        return "admin/especies";
    }

    // VER HABITATS
    @GetMapping("/habitats")
    public String verHabitats(Model model) {
        model.addAttribute("habitats", habitatService.listarHabitats());
        return "admin/habitats";
    }

    // VER ZONAS
    @GetMapping("/zonas")
    public String verZonas(Model model) {
        model.addAttribute("zonas", zonaService.listarZonas());
        return "admin/zonas";
    }

    // VER ITINERARIOS
    @GetMapping("/itinerarios")
    public String verItinerarios(Model model) {
        model.addAttribute("itinerarios", itinerarioService.listarItinerarios());
        return "admin/itinerarios";
    }

    // **** ZONAS ****
    // AGREGAR ZONA — muestra formulario 
    @GetMapping("/zonas/nueva")
    public String nuevaZonas(Model model) {
        model.addAttribute("zona", new Zona());
        return "admin/zonas/nueva";
    }

    // GUARDAR ZONA — guardar
    @PostMapping("/zonas/nueva")
    public String guardarZonas(@ModelAttribute Zona zona) {
        zonaService.guardar(zona);
        return "redirect:/admin/zonas";
    }

    // EDITAR ZONA — muestra formulario con datos
    @GetMapping("/zonas/editar/{id}")
    public String editarZona(@PathVariable Long id, Model model) {
        zonaService.buscarPorId(id).ifPresent(z -> model.addAttribute("zona", z));
        return "admin/zonas/editar";
    }

    // EDITAR ZONA — guarda cambios
    @PostMapping("/zonas/editar/{id}")
    public String actualizarZona(@PathVariable Long id, @ModelAttribute Zona zona) {
        zona.setId(id);
        zonaService.guardar(zona);
        return "redirect:/admin/zonas";
    }

    // ELIMINAR ZONA — soft delete
    /*@PostMapping("/zonas/eliminar/{id}")
    public String eliminarZona(@PathVariable Long id) {
        Optional<Zona> zona = zonaService.buscarPorId(id);
        if (zona.isPresent()) {// true si tiene algo a dentro, false si es nulo
            zonaService.eliminar(zona.get());
        }
        return "redirect:/admin/zonas";
    }*/
    @PostMapping("/zonas/eliminar/{id}")
    public String eliminarZona(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        Optional<Zona> zona = zonaService.buscarPorId(id);
        if (zona.isPresent()) {
            if (!zona.get().getItinerarios().isEmpty() || !zona.get().getEspecies().isEmpty()) {
                redirectAttrs.addFlashAttribute("error", 
                    "No se puede eliminar la zona porque tiene itinerarios y/o especies asociadas.");
                return "redirect:/admin/zonas";
            }
            zonaService.eliminar(zona.get());
        }
        return "redirect:/admin/zonas";
    }

    // **** HABITAT ****
    // AGREGAR HABITAT — muestra formulario
    @GetMapping("/habitats/nuevo")
    public String nuevoHabitat(Model model) {
        model.addAttribute("habitat", new Habitat());
        model.addAttribute("continentes", Continente.values());
        return "admin/habitats/nuevo";
    }

    // AGREGAR HABITAT — guarda
    @PostMapping("/habitats/nuevo")
    public String guardarHabitat(@ModelAttribute Habitat habitat,
                                @RequestParam(required = false) List<String> continentes) {
        if (continentes != null) {
            for (String c : continentes) {
                habitat.agregarContinente(Continente.valueOf(c));
            }
        }
        habitatService.guardar(habitat);
        return "redirect:/admin/habitats";
    }

    // EDITAR HABITAT — muestra formulario
    @GetMapping("/habitats/editar/{id}")
    public String editarHabitat(@PathVariable Long id, Model model) {
        habitatService.buscarPorId(id).ifPresent(h -> {
            model.addAttribute("habitat", h);
            model.addAttribute("continentes", Continente.values());
        });
        return "admin/habitats/editar";
    }

    // EDITAR HABITAT — guarda cambios
    @PostMapping("/habitats/editar/{id}")
    public String actualizarHabitat(@PathVariable Long id,
                                    @ModelAttribute Habitat habitat,
                                    @RequestParam(required = false) List<String> continentes) {
        habitat.setId(id);
        habitat.getContinentes().clear();
        if (continentes != null) {
            for (String c : continentes) {
                habitat.agregarContinente(Continente.valueOf(c));
            }
        }
        habitatService.guardar(habitat);
        return "redirect:/admin/habitats";
    }

    // ELIMINAR HABITAT — soft delete
    /*@PostMapping("/habitats/eliminar/{id}")
    public String eliminarHabitat(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        Optional<Habitat> habitat = habitatService.buscarPorId(id);
        if (habitat.isPresent()) {
            if (!habitat.get().getEspecies().isEmpty()) {
                redirectAttrs.addFlashAttribute("error", 
                    "No se puede eliminar el habitat porque tiene especies asociadas.");
                return "redirect:/admin/habitats";
            }
            habitatService.eliminar(habitat.get());
        }
        return "redirect:/admin/habitats";
    }*/
    @PostMapping("/habitats/eliminar/{id}")
    public String eliminarHabitat(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        Optional<Habitat> habitat = habitatService.buscarPorId(id);
        if (habitat.isPresent()) {
            boolean tieneEspeciesActivas = habitat.get().getEspecies().stream()
                    .anyMatch(Especie::isActivo);
            if (tieneEspeciesActivas) {
                redirectAttrs.addFlashAttribute("error", 
                    "No se puede eliminar el habitat porque tiene especies asociadas.");
                return "redirect:/admin/habitats";
            }
            habitatService.eliminar(habitat.get());
        }
        return "redirect:/admin/habitats";
    }

    // **** GUIA ****
    // AGREGAR GUIA — muestra formulario
    @GetMapping("/guias/nuevo")
    public String nuevoGuia(Model model) {
        model.addAttribute("guia", new Guia());
        return "admin/guias/nuevo";
    }

    // AGREGAR GUIA — guarda
    @PostMapping("/guias/nuevo")
    public String guardarGuia(@ModelAttribute Guia guia) {
        guia.setPassword(passwordEncoder.encode(guia.getPassword()));
        personaService.guardar(guia);
        return "redirect:/admin/personas";
    }

    // EDITAR GUIA — muestra formulario
    @GetMapping("/guias/editar/{id}")
    public String editarGuia(@PathVariable Long id, Model model) {
        personaService.buscarPorId(id).ifPresent(p -> model.addAttribute("guia", (Guia) p));
        return "admin/guias/editar";
    }

    // EDITAR GUIA — guarda cambios
    @PostMapping("/guias/editar/{id}")
    public String actualizarGuia(@PathVariable Long id, @ModelAttribute Guia guia, @RequestParam String passwordNueva) {
        guia.setId(id);
        if (passwordNueva != null && !passwordNueva.isEmpty()) {
            guia.setPassword(passwordEncoder.encode(passwordNueva));
        } else {
            // mantener la contraseña anterior
            String passwordActual = personaService.buscarPorId(id)
                    .map(Persona::getPassword).orElse("");
            guia.setPassword(passwordActual);
        }
        personaService.guardar(guia);
        return "redirect:/admin/personas";
    }

    // ELIMINAR GUIA — soft delete
    @PostMapping("/guias/eliminar/{id}")
    public String eliminarGuia(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        Optional<Persona> persona = personaService.buscarPorId(id);
        if (persona.isPresent()) {
            Guia guia = (Guia) persona.get();
            boolean tieneItinerariosActivos = guia.getAsignaciones().stream()
                    .anyMatch(a -> a.getItinerario().isActivo());
            if (tieneItinerariosActivos) {
                redirectAttrs.addFlashAttribute("error", 
                    "No se puede eliminar el guia porque tiene itinerarios asociados.");
                return "redirect:/admin/personas";
            }
            personaService.eliminar(guia);
        }
        return "redirect:/admin/personas";
    }

    // **** CUIDADOR ****
    // AGREGAR CUIDADOR — muestra formulario
    @GetMapping("/cuidadores/nuevo")
    public String nuevoCuidador(Model model) {
        model.addAttribute("cuidador", new Cuidador());
        return "admin/cuidadores/nuevo";
    }

    // AGREGAR CUIDADOR — guarda
    @PostMapping("/cuidadores/nuevo")
    public String guardarCuidador(@ModelAttribute Cuidador cuidador) {
        cuidador.setPassword(passwordEncoder.encode(cuidador.getPassword()));
        personaService.guardar(cuidador);
        return "redirect:/admin/personas";
    }

    // EDITAR CUIDADOR — muestra formulario
    @GetMapping("/cuidadores/editar/{id}")
    public String editarCuidador(@PathVariable Long id, Model model) {
        personaService.buscarPorId(id).ifPresent(p -> model.addAttribute("cuidador", (Cuidador) p));
        return "admin/cuidadores/editar";
    }

    // EDITAR CUIDADOR — guarda cambios
    @PostMapping("/cuidadores/editar/{id}")
    public String actualizarCuidador(@PathVariable Long id, @ModelAttribute Cuidador cuidador, @RequestParam String passwordNueva) {
        cuidador.setId(id);
        if (passwordNueva != null && !passwordNueva.isEmpty()) {
            cuidador.setPassword(passwordEncoder.encode(passwordNueva));
        } else {
            // mantener la contraseña anterior
            String passwordActual = personaService.buscarPorId(id)
                    .map(Persona::getPassword).orElse("");
            cuidador.setPassword(passwordActual);
        }
        personaService.guardar(cuidador);
        return "redirect:/admin/personas";
    }

    // ELIMINAR CUIDADOR — soft delete
    /*@PostMapping("/cuidadores/eliminar/{id}")
    public String eliminarCuidador(@PathVariable Long id) {
        Optional<Persona> persona = personaService.buscarPorId(id);
        if (persona.isPresent()) {
            personaService.eliminar(persona.get());
        }
        return "redirect:/admin/personas";
    }*/
       @PostMapping("/cuidadores/eliminar/{id}")
    public String eliminarCuidador(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        Optional<Persona> persona = personaService.buscarPorId(id);
        if (persona.isPresent()) {
            Cuidador cuidador = (Cuidador) persona.get();
            if (!cuidador.getCuidados().isEmpty()) {
                redirectAttrs.addFlashAttribute("error", 
                    "No se puede eliminar el cuidador porque tiene especies asociadas.");
                return "redirect:/admin/personas";
            }
            personaService.eliminar(cuidador);
        }
        return "redirect:/admin/personas";
    }

    // **** ESPECIE ****
    // AGREGAR ESPECIE — muestra formulario
    @GetMapping("/especies/nueva")
    public String nuevaEspecie(Model model) {
        model.addAttribute("especie", new Especie());
        model.addAttribute("zonas", zonaService.listarZonas());
        model.addAttribute("habitats", habitatService.listarHabitats());
        model.addAttribute("cuidadores", personaService.listarCuidadores());
        return "admin/especies/nueva";
    }

    // AGREGAR ESPECIE — guarda
    @PostMapping("/especies/nueva")
    public String guardarEspecie(@ModelAttribute Especie especie,
                                @RequestParam Long zonaId,
                                @RequestParam(required = false) List<Long> habitatIds,
                                @RequestParam(required = false) List<Long> cuidadorIds) {
        Zona zona = zonaService.buscarPorId(zonaId).orElse(null);
        especie.setZona(zona);
        
        if (habitatIds != null) {
            for (Long id : habitatIds) {
                Habitat habitat = habitatService.buscarPorId(id).orElse(null);
                if (habitat != null) {
                    especie.agregarHabitat(habitat);
                }
            }
        }
        if (zona != null) zona.agregarEspecie(especie);
        especieService.guardar(especie);
        if (cuidadorIds != null) {
            for (Long id : cuidadorIds) {
                Cuidador cuidador = (Cuidador) personaService.buscarPorId(id).orElse(null);
                if (cuidador != null) {
                    Cuidado cuidado = new Cuidado(cuidador, especie, LocalDate.now().toString());
                    especie.agregarCuidador(cuidado);
                    cuidador.agregarEspecie(cuidado);
                    cuidadoService.guardar(cuidado);
                }
            }
        }
        return "redirect:/admin/especies";
    }

    // EDITAR ESPECIE — muestra formulario
    @GetMapping("/especies/editar/{id}")
    public String editarEspecie(@PathVariable Long id, Model model) {
        especieService.buscarPorId(id).ifPresent(e -> {
            model.addAttribute("especie", e);
            model.addAttribute("zonas", zonaService.listarZonas());
            model.addAttribute("habitats", habitatService.listarHabitats());
            model.addAttribute("cuidadores", personaService.listarCuidadores());

            List<Long> cuidadorIdsAsignados = e.getCuidados().stream()
                .map(c -> c.getCuidador().getId())
                .toList();
            model.addAttribute("cuidadorIdsAsignados", cuidadorIdsAsignados);
        });
        return "admin/especies/editar";
    }

    // EDITAR ESPECIE — guarda cambios
    @PostMapping("/especies/editar/{id}")
    public String actualizarEspecie(@PathVariable Long id,
                                    @ModelAttribute Especie especie,
                                    @RequestParam Long zonaId,
                                    @RequestParam(required = false) List<Long> habitatIds,
                                    @RequestParam(required = false) List<Long> cuidadorIds) {
        especie.setId(id);
        // Zona
        Zona zona = zonaService.buscarPorId(zonaId).orElse(null);
        especie.setZona(zona);

        // Habitat
        especie.getHabitats().clear();
        if (habitatIds != null) {
            for (Long idHabitat : habitatIds) {
                habitatService.buscarPorId(idHabitat).ifPresent(especie::agregarHabitat);
            }
        }

        especieService.guardar(especie);

        Especie especieDb = especieService.buscarPorId(id).orElse(especie);
        List<Long> actuales = especieDb.getCuidados().stream()
                .map(c -> c.getCuidador().getId())
                .toList();
        List<Long> nuevos = cuidadorIds != null ? cuidadorIds : List.of();

        // sacar los que ya no están tildados
        for (Cuidado cuidado : new ArrayList<>(especieDb.getCuidados())) {
            if (!nuevos.contains(cuidado.getCuidador().getId())) {
                cuidadoService.eliminar(cuidado);
            }
        }
        // agregar los que son nuevos
        for (Long cuidadorId : nuevos) {
            if (!actuales.contains(cuidadorId)) {
                Cuidador cuidador = (Cuidador) personaService.buscarPorId(cuidadorId).orElse(null);
                if (cuidador != null) {
                    Cuidado cuidado = new Cuidado(cuidador, especieDb, LocalDate.now().toString());
                    cuidadoService.guardar(cuidado);
                }
            }
        }
        return "redirect:/admin/especies";
    }

    // ELIMINAR ESPECIE — soft delete
    @PostMapping("/especies/eliminar/{id}")
    public String eliminarEspecie(@PathVariable Long id) {
        Optional<Especie> especie = especieService.buscarPorId(id);
        if (especie.isPresent()) {
            especieService.eliminar(especie.get());
        }
        return "redirect:/admin/especies";
    }

    // **** ITINERARIO ****
    // AGREGAR ITINERARIO — muestra formulario
    @GetMapping("/itinerarios/nuevo")
    public String nuevoItinerario(Model model) {
        model.addAttribute("itinerario", new Itinerario());
        model.addAttribute("zonas", zonaService.listarZonas());
        model.addAttribute("guias", personaService.listarGuias());
        return "admin/itinerarios/nuevo";
    }

    // AGREGAR ITINERARIO — guarda
    @PostMapping("/itinerarios/nuevo")
    public String guardarItinerario(@ModelAttribute Itinerario itinerario,
                                @RequestParam List<Long> zonaIds,
                                @RequestParam(required = false) List<Long> guiaIds) {
        for (Long zid : zonaIds) {
            Zona zona = zonaService.buscarPorId(zid).orElse(null);
            if (zona != null) {
                itinerario.agregarZona(zona);
                zona.agregarItinerario(itinerario);
            }
        }
        itinerarioService.guardar(itinerario);

        if (guiaIds != null) {
            for (Long gid : guiaIds) {
                Guia guia = (Guia) personaService.buscarPorId(gid).orElse(null);
                if (guia != null) {
                    Asignacion asignacion = new Asignacion(guia, itinerario, java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                    itinerario.agregarAsignacion(asignacion);
                    guia.agregarAsignacion(asignacion);
                    asignacionService.guardar(asignacion);
                }
            }
        }
        return "redirect:/admin/itinerarios";
    }

    // EDITAR ITINERARIO — muestra formulario
    @GetMapping("/itinerarios/editar/{id}")
    public String editarItinerario(@PathVariable Long id, Model model) {
        itinerarioService.buscarPorId(id).ifPresent(i -> {
            model.addAttribute("itinerario", i);
            model.addAttribute("zonas", zonaService.listarZonas());
            model.addAttribute("guias", personaService.listarGuias());

            List<Long> guiaIdsAsignados = i.getAsignaciones().stream()
                .map(a -> a.getGuia().getId())
                .toList();
            model.addAttribute("guiaIdsAsignados", guiaIdsAsignados);
        });
        return "admin/itinerarios/editar";
    }

    // EDITAR ITINERARIO — guarda cambios
    /*@PostMapping("/itinerarios/editar/{id}")
    public String actualizarItinerario(@PathVariable Long id,
                                    @ModelAttribute Itinerario itinerario,
                                    @RequestParam List<Long> zonaIds,
                                    @RequestParam(required = false) List<Long> guiaIds) {
        itinerario.setId(id);

        for (Long zid : zonaIds) {
            zonaService.buscarPorId(zid).ifPresent(itinerario::agregarZona);
        }
        itinerarioService.guardar(itinerario);

        Itinerario itinerarioDb = itinerarioService.buscarPorId(id).orElse(itinerario);
        List<Long> actuales = itinerarioDb.getAsignaciones().stream()
                .map(a -> a.getGuia().getId())
                .toList();
        List<Long> nuevos = guiaIds != null ? guiaIds : List.of();

        for (Asignacion asig : new ArrayList<>(itinerarioDb.getAsignaciones())) {
            if (!nuevos.contains(asig.getGuia().getId())) {
                asignacionService.eliminar(asig);
            }
        }
        for (Long gid : nuevos) {
            if (!actuales.contains(gid)) {
                Guia guia = (Guia) personaService.buscarPorId(gid).orElse(null);
                if (guia != null) {
                    Asignacion asignacion = new Asignacion(guia, itinerarioDb, java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                    asignacionService.guardar(asignacion);
                }
            }
        }
        return "redirect:/admin/itinerarios";
    }*/
    @PostMapping("/itinerarios/editar/{id}")
    public String actualizarItinerario(@PathVariable Long id,
                                    @ModelAttribute Itinerario itinerario,
                                    @RequestParam List<Long> zonaIds,
                                    @RequestParam(required = false) List<Long> guiaIds) {

        // Obtener el itinerario existente de la BD
        Itinerario existente = itinerarioService.buscarPorId(id).orElse(null);
        if (existente == null) return "redirect:/admin/itinerarios";

        // Copiar los datos nuevos al existente
        existente.setCodigo(itinerario.getCodigo());
        existente.setDuracionRecorrido(itinerario.getDuracionRecorrido());
        existente.setLongitud(itinerario.getLongitud());
        existente.setMaxVisitantes(itinerario.getMaxVisitantes());

        // Limpiar zonas anteriores y agregar las nuevas
        existente.getZonas().clear();
        for (Long zid : zonaIds) {
            zonaService.buscarPorId(zid).ifPresent(existente::agregarZona);
        }
        itinerarioService.guardar(existente);

        // Manejar asignaciones de guias
        List<Long> actuales = existente.getAsignaciones().stream()
                .map(a -> a.getGuia().getId())
                .toList();
        List<Long> nuevos = guiaIds != null ? guiaIds : List.of();

        // Eliminar asignaciones que ya no están
        for (Asignacion asig : new ArrayList<>(existente.getAsignaciones())) {
            if (!nuevos.contains(asig.getGuia().getId())) {
                asignacionService.eliminar(asig);
            }
        }

        // Agregar nuevas asignaciones
        for (Long gid : nuevos) {
            if (!actuales.contains(gid)) {
                Guia guia = (Guia) personaService.buscarPorId(gid).orElse(null);
                if (guia != null) {
                    Asignacion asignacion = new Asignacion(guia, existente,
                        java.time.LocalTime.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                    asignacionService.guardar(asignacion);
                }
            }
        }

        return "redirect:/admin/itinerarios";
    }

    // ELIMINAR ITINERARIO — soft delete
    @PostMapping("/itinerarios/eliminar/{id}")
    public String eliminarItinerario(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        Optional<Itinerario> itinerario = itinerarioService.buscarPorId(id);
        if (itinerario.isPresent()) {
            Itinerario i = itinerario.get();
            // Limpiar relaciones antes de eliminar
            i.getZonas().clear();
            // Eliminar asignaciones
            for (Asignacion a : new ArrayList<>(i.getAsignaciones())) {
                asignacionService.eliminar(a);
            }
            itinerarioService.guardar(i); // guarda con las listas vacías
            itinerarioService.eliminar(i); // ahora sí hace el soft delete
        }
        return "redirect:/admin/itinerarios";
    }
}