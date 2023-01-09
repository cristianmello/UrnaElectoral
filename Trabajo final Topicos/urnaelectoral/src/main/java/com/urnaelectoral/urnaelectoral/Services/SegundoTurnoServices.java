package com.urnaelectoral.urnaelectoral.Services;

import java.util.List;
import java.util.Optional;

import com.urnaelectoral.urnaelectoral.Model.SegundoTurno;



public interface SegundoTurnoServices {

    List<SegundoTurno> findAll();

    Optional<SegundoTurno> findById(Integer id);

    SegundoTurno save(SegundoTurno segundoTurnoId);   

    public void deleteById(Integer segundoTurnoId);

}
