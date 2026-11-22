package pe.edu.certus.educontrol.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import pe.edu.certus.educontrol.repository.AlumnoRepository;
import pe.edu.certus.educontrol.model.Alumno;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoRepository alumnoRepository;

    public AlumnoController(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    @GetMapping
    public List<Alumno> listarTodos() {
        return alumnoRepository.findAll();
    }
}