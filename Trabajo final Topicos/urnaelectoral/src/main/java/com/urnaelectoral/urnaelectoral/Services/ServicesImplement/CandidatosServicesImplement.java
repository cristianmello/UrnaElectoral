package com.urnaelectoral.urnaelectoral.Services.ServicesImplement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urnaelectoral.urnaelectoral.Model.Candidatos;
import com.urnaelectoral.urnaelectoral.Repository.CandidatosRepository;
import com.urnaelectoral.urnaelectoral.Services.CandidatosServices;

@Service
public class CandidatosServicesImplement implements CandidatosServices {

	@Autowired
	private CandidatosRepository repository;

	@Override
	public List<Candidatos> findAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Candidatos> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public Candidatos save(Candidatos candidato) {
		return repository.save(candidato);
	}

	@Override
	public void deleteById(Integer codigoCandidato) {
		repository.deleteById(codigoCandidato);
	}

	@Override
	public String deleteCandidatos() {
		repository.deleteAll();
		return "OK";
	}

	
}
