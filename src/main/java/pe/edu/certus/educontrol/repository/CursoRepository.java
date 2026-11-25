package pe.edu.certus.educontrol.repository;

import pe.edu.certus.educontrol.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
