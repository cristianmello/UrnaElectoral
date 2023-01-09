package com.urnaelectoral.urnaelectoral.Services.ServicesImplement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urnaelectoral.urnaelectoral.Model.PrimerTurno;
import com.urnaelectoral.urnaelectoral.Repository.PrimerTurnoRepository;
import com.urnaelectoral.urnaelectoral.Services.PrimerTurnoServices;

@Service
public class PrimerTurnoServicesImplement implements PrimerTurnoServices {

    @Autowired
    private PrimerTurnoRepository repository;

    @Override
    public List<PrimerTurno> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PrimerTurno> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public PrimerTurno save(PrimerTurno candPrimerTurno) {
        return repository.save(candPrimerTurno);
    }

    @Override
    public void deleteById(Integer primerTurnoId) {
        repository.deleteById(primerTurnoId);
    }

}
