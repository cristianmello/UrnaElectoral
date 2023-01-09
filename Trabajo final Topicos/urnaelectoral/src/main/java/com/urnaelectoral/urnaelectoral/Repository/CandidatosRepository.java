package com.urnaelectoral.urnaelectoral.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urnaelectoral.urnaelectoral.Model.Candidatos;


public interface CandidatosRepository extends JpaRepository<Candidatos, Integer> {
    
}
