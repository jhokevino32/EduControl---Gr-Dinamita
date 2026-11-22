package pe.edu.certus.educontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.certus.educontrol.model.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}