package pe.edu.certus.educontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.certus.educontrol.model.Asistencia;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    
    List<Asistencia> findByAlumnoId(Long alumnoId);
    
    List<Asistencia> findByCursoId(Long cursoId);
    
   
    List<Asistencia> findByCurso_IdAndFecha(Long cursoId, LocalDate fecha);
}