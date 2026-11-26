package pe.edu.certus.educontrol.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.certus.educontrol.model.Alumno;
import pe.edu.certus.educontrol.model.Asistencia;
import pe.edu.certus.educontrol.model.Curso;
import pe.edu.certus.educontrol.repository.AlumnoRepository;
import pe.edu.certus.educontrol.repository.AsistenciaRepository;
import pe.edu.certus.educontrol.repository.CursoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("PRESENTE", "TARDANZA", "FALTA");

    private final AsistenciaRepository asistenciaRepository;
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;

    public AsistenciaController(
            AsistenciaRepository asistenciaRepository,
            AlumnoRepository alumnoRepository,
            CursoRepository cursoRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.alumnoRepository = alumnoRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public List<Asistencia> listarTodos() {
        return asistenciaRepository.findAll();
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<Asistencia> listarPorAlumno(@PathVariable Long alumnoId) {
        return asistenciaRepository.findByAlumnoId(alumnoId);
    }

    @GetMapping("/curso/{cursoId}")
    public List<Asistencia> listarPorCurso(
            @PathVariable Long cursoId,
            @RequestParam(required = false) LocalDate fecha) {
        if (fecha != null) {
            
            return asistenciaRepository.findByCurso_IdAndFecha(cursoId, fecha);
        }
        return asistenciaRepository.findByCursoId(cursoId);
    }

    @PostMapping
    public ResponseEntity<?> registrarAsistencia(@RequestBody RegistroAsistenciaRequest request) {
        if (request.alumnoId() == null || request.cursoId() == null || request.fecha() == null || request.estado() == null) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios (alumnoId, cursoId, fecha, estado).");
        }

        String estadoNormalizado = request.estado().trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estadoNormalizado)) {
            return ResponseEntity.badRequest().body("Estado inválido. Use: PRESENTE, TARDANZA o FALTA.");
        }

        Alumno alumno = alumnoRepository.findById(request.alumnoId()).orElse(null);
        if (alumno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alumno no encontrado.");
        }

        Curso curso = cursoRepository.findById(request.cursoId()).orElse(null);
        if (curso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso no encontrado.");
        }

        Asistencia asistencia = new Asistencia(alumno, curso, request.fecha(), estadoNormalizado);
        Asistencia guardada = asistenciaRepository.save(asistencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    public record RegistroAsistenciaRequest(
            Long alumnoId,
            Long cursoId,
            LocalDate fecha,
            String estado
    ) {}
}