package com.aws.awsproject.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.aws.awsproject.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
