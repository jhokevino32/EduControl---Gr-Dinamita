package pe.edu.certus.educontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.java.pe.edu.certus.educontrol.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}