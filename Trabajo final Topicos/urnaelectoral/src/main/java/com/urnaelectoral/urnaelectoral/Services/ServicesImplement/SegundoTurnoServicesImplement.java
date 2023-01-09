package com.urnaelectoral.urnaelectoral.Services.ServicesImplement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urnaelectoral.urnaelectoral.Model.SegundoTurno;
import com.urnaelectoral.urnaelectoral.Repository.SegundoTurnoRepository;
import com.urnaelectoral.urnaelectoral.Services.SegundoTurnoServices;

@Service
public class SegundoTurnoServicesImplement implements SegundoTurnoServices {

    @Autowired
    private SegundoTurnoRepository repository;

    @Override
    public List<SegundoTurno> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<SegundoTurno> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public SegundoTurno save(SegundoTurno candSegundoTurno) {
        return repository.save(candSegundoTurno);
    }

    @Override
    public void deleteById(Integer segundoTurnoId) {
        repository.deleteById(segundoTurnoId);
    }

}
