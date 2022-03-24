package ito.dsos.evaluacion.Controller;

import ito.dsos.evaluacion.Entity.Student;
import ito.dsos.evaluacion.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @PostMapping("/post")
    public ResponseEntity<Map<String,String>> getICA(@ModelAttribute Student newStudent){
        String niveles[] = {"Obesidad mórbida", "Sobrepeso elevado", "Sobrepeso",
                "Peso normal", "Delgadez leve", "Delgadez severa"};
        double hombre[] = {0.63, 0.58, 0.53, 0.43, 0.35, 0};
        double mujer[] = {0.58, 0.54, 0.49, 0.42, 0.35, 0};
        Student student;
        try {
            student = studentService.getByControlNumber(newStudent.getControlNumber())
                    .orElseThrow(() -> new IllegalStateException("ERROR"));
            if (newStudent.getAlturaCm() != null && newStudent.getAlturaCm() != 0)
                student.setAlturaCm(newStudent.getAlturaCm());
            if (newStudent.getCinturaCm() != null && newStudent.getCinturaCm() != 0)
                student.setCinturaCm(newStudent.getCinturaCm());
            if (student.getGender() == null && newStudent.getGender() != null)
                student.setGender(newStudent.getGender());
            student = studentService.save(student);
        }catch (IllegalStateException e){
            if(newStudent == null) {
                HashMap<String, String> map = new HashMap<>();
                map.put("error", "No se cuentan con los datos necesarios para el cálculo");
                return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
            }
            student = studentService.save(newStudent);
        }
        boolean verifyAltura = student.getAlturaCm() == null || student.getAlturaCm() == 0;
        boolean verifyCintura = student.getCinturaCm() == null || student.getCinturaCm() == 0;
        if (verifyAltura || verifyCintura){
            HashMap<String, String> map = new HashMap<>();
            map.put("error", "Los valores enviados no son válidos");
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
        if (student.getGender() == null){
            HashMap<String, String> map = new HashMap<>();
            map.put("error", "No se envió un valor válido para genero (H, M)");
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
        double ICA = student.getCinturaCm() / student.getAlturaCm();
        String resultado = "";
        double indices[] = (student.getGender() == 'H')? hombre:mujer;
        for(int i=0; i< niveles.length;i++){
            if (ICA>indices[i]){
                resultado = niveles[i];
                break;
            }}
        HashMap<String, String> map = new HashMap<>();
        map.put("ICA", String.valueOf(ICA));
        map.put("resultado", resultado);
        map.put("status", "OK");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
