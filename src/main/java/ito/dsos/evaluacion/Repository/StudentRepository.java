package ito.dsos.evaluacion.Repository;

import ito.dsos.evaluacion.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByControlNumber(String controlNumber);
}
