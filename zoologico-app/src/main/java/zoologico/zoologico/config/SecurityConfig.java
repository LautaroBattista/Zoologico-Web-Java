package zoologico.zoologico.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import zoologico.zoologico.service.PersonaService;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PersonaService personaService;

    @Bean
    //SecurityFilterChain es una interfaz de Spring Security que representa la cadena de filtros de seguridad — básicamente todas las reglas que configuraste adentro del método.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/", "/login", "/setup/**").permitAll() // Accesible para todos sin login, es para el primer arranque del sistema cuando no hay ningún admin.
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/guia/**").hasAnyRole("GUIA", "ADMINISTRADOR")
                .requestMatchers("/cuidador/**").hasAnyRole("CUIDADOR", "ADMINISTRADOR")
                .anyRequest().authenticated() // todo lo demas requiere login
            )
            .formLogin(form -> form // activa el login
                .loginPage("/login") // pagina de login
                .usernameParameter("username")
                .passwordParameter("password")
                // request es la petición HTTP que hizo el usuario (el formulario que envió)
                // response — la respuesta que vas a mandar de vuelta al navegador
                // authentication — información del usuario que se logueó (su rol, su usuario, etc.)
                .successHandler((request, response, authentication) -> {
                    String role = authentication.getAuthorities()// devuelve la lista de roles del usuario (que es solo un rol)
                                    .iterator().next().getAuthority();// .iterator().next() -> agarra el primero de la lista (el unico rol)
                                    // .getAuthority() -> obtiene el nombre del rol como String 
                    switch (role) {
                        case "ROLE_ADMINISTRADOR":
                            response.sendRedirect("/admin/inicio");
                            break;
                        case "ROLE_GUIA":
                            response.sendRedirect("/guia/inicio");
                            break;
                        case "ROLE_CUIDADOR":
                            response.sendRedirect("/cuidador/inicio");
                            break;
                        default:
                            response.sendRedirect("/login?error");
                            break;
                    }
                })
                .permitAll()
            )
            .logout(logout -> logout // activa el logout
                .permitAll()
            );
        return http.build();
    }

    // Verifica usuario y contraseña cuando alguien se loguea
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class); // Dame el objeto de tipo AuthenticationManagerBuilder que tenés guardado internamente
        authBuilder
            .userDetailsService(personaService) // Busca al usuario en personaService
            .passwordEncoder(passwordEncoder()); // Verificar la contraseña con BCrypt
        return authBuilder.build(); // Construye y retorna el AuthenticationManager configurado
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

