package com.aws.awsproject.service;

import com.aws.awsproject.dto.StudentDTO;
import com.aws.awsproject.entity.Student;
import com.aws.awsproject.entity.SessionAlumno;
import com.aws.awsproject.exception.ResourceNotFoundException;
import com.aws.awsproject.repository.StudentRepository;
import com.aws.awsproject.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final S3Service s3Service;
    private final SnsService snsService;
    private final DynamoDbService dynamoDbService;
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setNombres(StringUtils.cleanAndCapitalize(studentDTO.getNombres()));
        student.setApellidos(StringUtils.cleanAndCapitalize(studentDTO.getApellidos()));
        student.setMatricula(StringUtils.cleanAndUpperCase(studentDTO.getMatricula()));
        student.setPromedio(studentDTO.getPromedio());
        student.setPassword(studentDTO.getPassword()); // TODO: CHANGE THIS FOR FUTURE ADJANY Xd !!!
        return studentRepository.save(student);
    }

    public Student updateStudent(Integer id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        existingStudent.setNombres(studentDTO.getNombres());
        existingStudent.setApellidos(studentDTO.getApellidos());
        existingStudent.setMatricula(studentDTO.getMatricula());
        existingStudent.setPromedio(studentDTO.getPromedio());

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Integer id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
    public Student uploadProfilePicture(Integer id, MultipartFile file) throws IOException {
        Student student = getStudentById(id);
        String url = s3Service.uploadFile(file, id);
        student.setFotoPerfilUrl(url);
        return studentRepository.save(student);
    }

    public void sendEmail(Integer id) {
        Student student = getStudentById(id);
        snsService.sendStudentEmail(student);
    }

    public SessionAlumno login(Integer id, String password) {
        Student student = getStudentById(id);
        if (!student.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return dynamoDbService.createSession(id);
    }

    public boolean verifySession(Integer id, String sessionString) {
        return dynamoDbService.verifySession(id, sessionString);
    }

    public void logout(Integer id, String sessionString) {
        dynamoDbService.logoutSession(id, sessionString);
    }
}
