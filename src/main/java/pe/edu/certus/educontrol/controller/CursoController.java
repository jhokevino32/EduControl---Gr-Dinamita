package pe.edu.certus.educontrol.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import main.java.pe.edu.certus.educontrol.model.Curso;
import main.java.pe.edu.certus.educontrol.repository.CursoRepository;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoRepository cursoRepository;

    public CursoController(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }
}
