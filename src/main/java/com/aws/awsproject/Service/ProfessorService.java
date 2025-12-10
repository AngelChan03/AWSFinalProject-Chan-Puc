package com.aws.awsproject.service;

import com.aws.awsproject.dto.ProfessorDTO;
import com.aws.awsproject.entity.Professor;
import com.aws.awsproject.exception.ResourceNotFoundException;
import com.aws.awsproject.repository.ProfessorRepository;
import com.aws.awsproject.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public Professor getProfessorById(Integer id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + id));
    }

    public Professor createProfessor(ProfessorDTO professorDTO) {
        Professor professor = new Professor();
        professor.setNumeroEmpleado(professorDTO.getNumeroEmpleado());
        professor.setNombres(StringUtils.cleanAndCapitalize(professorDTO.getNombres()));
        professor.setApellidos(StringUtils.cleanAndCapitalize(professorDTO.getApellidos()));
        professor.setHorasClase(professorDTO.getHorasClase());
        return professorRepository.save(professor);
    }

    public Professor updateProfessor(Integer id, ProfessorDTO professorDTO) {
        Professor professor = getProfessorById(id);
        professor.setNumeroEmpleado(professorDTO.getNumeroEmpleado());
        professor.setNombres(StringUtils.cleanAndCapitalize(professorDTO.getNombres()));
        professor.setApellidos(StringUtils.cleanAndCapitalize(professorDTO.getApellidos()));
        professor.setHorasClase(professorDTO.getHorasClase());
        return professorRepository.save(professor);
    }

    public void deleteProfessor(Integer id) {
        if (!professorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Professor not found with id: " + id);
        }
        professorRepository.deleteById(id);
    }
}