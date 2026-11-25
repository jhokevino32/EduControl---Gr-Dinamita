
package pe.edu.certus.educontrol.controller;

import pe.edu.certus.educontrol.model.Alumno;
import pe.edu.certus.educontrol.model.Curso;
import pe.edu.certus.educontrol.model.Nota;
import pe.edu.certus.educontrol.repository.AlumnoRepository;
import pe.edu.certus.educontrol.repository.CursoRepository;
import pe.edu.certus.educontrol.repository.NotaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;

    public NotaController(
            NotaRepository notaRepository,
            AlumnoRepository alumnoRepository,
            CursoRepository cursoRepository) {
        this.notaRepository = notaRepository;
        this.alumnoRepository = alumnoRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public List<Nota> listarNotas() {
        return notaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nota> buscarNota(@PathVariable Long id) {
        return notaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<Nota> listarPorAlumno(@PathVariable Long alumnoId) {
        return notaRepository.findByAlumno_Id(alumnoId);
    }

    @GetMapping("/curso/{cursoId}")
    public List<Nota> listarPorCurso(@PathVariable Long cursoId) {
        return notaRepository.findByCurso_Id(cursoId);
    }

    @GetMapping("/alumno/{alumnoId}/curso/{cursoId}/promedio")
    public ResponseEntity<?> obtenerPromedio(
            @PathVariable Long alumnoId,
            @PathVariable Long cursoId) {

        List<Nota> notas = notaRepository
                .findByAlumno_IdAndCurso_Id(alumnoId, cursoId);

        if (notas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        double promedio = notas.stream()
                .mapToInt(Nota::getValor)
                .average()
                .orElse(0);

        return ResponseEntity.ok(Map.of("promedio", promedio));
    }

    @PostMapping
    public ResponseEntity<?> registrarNota(@RequestBody NotaRequest request) {

        if (request.valor() == null || request.valor() < 0 || request.valor() > 20) {
            return ResponseEntity.badRequest()
                    .body("La nota debe estar entre 0 y 20.");
        }

        Alumno alumno = alumnoRepository.findById(request.alumnoId())
                .orElse(null);

        if (alumno == null) {
            return ResponseEntity.badRequest()
                    .body("El alumno no existe.");
        }

        Curso curso = cursoRepository.findById(request.cursoId())
                .orElse(null);

        if (curso == null) {
            return ResponseEntity.badRequest()
                    .body("El curso no existe.");
        }

        Nota nota = new Nota();
        nota.setAlumno(alumno);
        nota.setCurso(curso);
        nota.setValor(request.valor());
        nota.setObservacion(request.observacion());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notaRepository.save(nota));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarNota(
            @PathVariable Long id,
            @RequestBody NotaRequest request) {

        Nota nota = notaRepository.findById(id).orElse(null);

        if (nota == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.valor() == null || request.valor() < 0 || request.valor() > 20) {
            return ResponseEntity.badRequest()
                    .body("La nota debe estar entre 0 y 20.");
        }

        Alumno alumno = alumnoRepository.findById(request.alumnoId())
                .orElse(null);

        if (alumno == null) {
            return ResponseEntity.badRequest()
                    .body("El alumno no existe.");
        }

        Curso curso = cursoRepository.findById(request.cursoId())
                .orElse(null);

        if (curso == null) {
            return ResponseEntity.badRequest()
                    .body("El curso no existe.");
        }

        nota.setAlumno(alumno);
        nota.setCurso(curso);
        nota.setValor(request.valor());
        nota.setObservacion(request.observacion());

        return ResponseEntity.ok(notaRepository.save(nota));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNota(@PathVariable Long id) {

        if (!notaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        notaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record NotaRequest(
            Long alumnoId,
            Long cursoId,
            Integer valor,
            String observacion) {
    }
}
