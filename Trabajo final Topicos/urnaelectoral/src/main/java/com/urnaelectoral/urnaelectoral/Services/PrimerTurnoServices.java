package com.urnaelectoral.urnaelectoral.Services;

import java.util.List;
import java.util.Optional;

import com.urnaelectoral.urnaelectoral.Model.PrimerTurno;


public interface PrimerTurnoServices {

    List<PrimerTurno> findAll();

    Optional<PrimerTurno> findById(Integer id);

    PrimerTurno save(PrimerTurno primerTurnoId);   

    public void deleteById(Integer primerTurnoId);


}
