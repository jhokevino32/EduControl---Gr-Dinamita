
package pe.edu.certus.educontrol.repository;

import pe.edu.certus.educontrol.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    List<Nota> findByAlumno_Id(Long alumnoId);

    List<Nota> findByCurso_Id(Long cursoId);

    List<Nota> findByAlumno_IdAndCurso_Id(
        Long alumnoId, Long cursoId
    );
}
