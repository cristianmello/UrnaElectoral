package com.urnaelectoral.urnaelectoral.Services;

import java.util.List;
import java.util.Optional;

import com.urnaelectoral.urnaelectoral.Model.Candidatos;

public interface CandidatosServices {

    List<Candidatos> findAll();

    Optional<Candidatos> findById(Integer codigoCandidato);

    Candidatos save(Candidatos candidato);

    public void deleteById(Integer codigoCandidato);

    public String deleteCandidatos();

}
