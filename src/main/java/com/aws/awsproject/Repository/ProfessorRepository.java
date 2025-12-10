package com.aws.awsproject.repository;

import java.util.List;
import java.util.Optional;

import com.aws.awsproject.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {

}