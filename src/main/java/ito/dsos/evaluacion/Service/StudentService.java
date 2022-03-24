package ito.dsos.evaluacion.Service;

import ito.dsos.evaluacion.Entity.Student;
import ito.dsos.evaluacion.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student student){
        return studentRepository.save(student);
    }

    public Optional<Student> getById(Long id){
        return studentRepository.findById(id);
    }

    public Optional<Student> getByControlNumber(String controlNumber){
        return studentRepository.findByControlNumber(controlNumber);
    }
}
