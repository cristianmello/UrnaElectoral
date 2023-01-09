package com.urnaelectoral.urnaelectoral.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.urnaelectoral.urnaelectoral.Model.Candidatos;
import com.urnaelectoral.urnaelectoral.Repository.CandidatosRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidatos")
public class ControllerCandidatos {

	@Autowired
	private CandidatosRepository candidatoRepository;

	// Post
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Candidatos candidato) {
		return ResponseEntity.status(HttpStatus.CREATED).body(candidatoRepository.save(candidato));
	}

	// Get
	@GetMapping("/api/{id}")
	public ResponseEntity<?> read(@PathVariable int id) {
		Optional<Candidatos> oCandidato = candidatoRepository.findById(id);

		if (!oCandidato.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(oCandidato);
	}

	// Update
	@PutMapping("/api/{id}")
	public ResponseEntity<?> update(@RequestBody Candidatos candidatoDetalles,
			@PathVariable(value = "id") int codigoCandidato) {
		Optional<Candidatos> oCandidatos = candidatoRepository.findById(codigoCandidato);

		if (!oCandidatos.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		oCandidatos.get().setNombreCandidato(candidatoDetalles.getNombreCandidato());

		return ResponseEntity.status(HttpStatus.CREATED).body(candidatoRepository.save(oCandidatos.get()));

	}

	// Delete Candidato
	@DeleteMapping("/api/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") int candidatoId) {

		if (!candidatoRepository.findById(candidatoId).isPresent()) {
			return ResponseEntity.notFound().build();
		}

		candidatoRepository.deleteById(candidatoId);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/home")
	public ModelAndView getHome() {
		ModelAndView mv = new ModelAndView("home");
		List<Candidatos> candidatos = candidatoRepository.findAll();

		mv.addObject("candidatos", candidatos);

		return mv;

	}

	@GetMapping("/registrarCandidato")
	public ModelAndView getCandidatos() {
		ModelAndView mv = new ModelAndView("registroCandidato");
		List<Candidatos> candidatos = candidatoRepository.findAll();
		mv.addObject("candidatos", candidatos);

		return mv;
	}

	@PostMapping("/registrarCandidato")
	public ModelAndView registrarCandidatos(@Valid Candidatos candidatos, BindingResult result,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagemError", "Verifique os campos obrigatorios");
			return new ModelAndView("redirect:/candidatos/registrarCandidato");
		}

		attributes.addFlashAttribute("mensagemRecibida", "Candidato registrado con exito");

		candidatoRepository.save(candidatos);

		return new ModelAndView("redirect:/candidatos/registrarCandidato");
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteCandidato(@PathVariable("id") int id, RedirectAttributes attributes) {
		try {
			candidatoRepository.deleteById(id);
			attributes.addFlashAttribute("mensagemRecibida", "El candidato ha sido eliminado con exito");

		} catch (Exception e) {
			attributes.addFlashAttribute("mensagemError",
					"El candidato se encuentra registrado en alguna fase de elecciones!");
		}
		return new ModelAndView("redirect:/candidatos/home");
	}

	@RequestMapping(value = "/update/{codigoCandidato}", method = RequestMethod.GET)
	public ModelAndView updateCandidato(@PathVariable("codigoCandidato") int id) {
		ModelAndView mv = new ModelAndView("actualizarCandidato");
		Optional<Candidatos> oCandidatos = candidatoRepository.findById(id);
		mv.addObject("nombreCandidato", oCandidatos.get().getNombreCandidato());
		mv.addObject("tipoCargo", oCandidatos.get().getTipoCargo());

		return mv;
	}

	@RequestMapping(value = "/update/{codigoCandidato}", method = RequestMethod.POST)
	public ModelAndView updateEmpleo(Candidatos candidatos, BindingResult result, RedirectAttributes atributes) {

		Candidatos candidatoExistente = candidatoRepository.findById(candidatos.getCodigoCandidato()).orElse(null);

		candidatoExistente.setNombreCandidato(candidatos.getNombreCandidato());
		candidatoExistente.setTipoCargo(candidatos.getTipoCargo());

		try {
			candidatoRepository.save(candidatoExistente);
			atributes.addFlashAttribute("mensagemRecibida", "Actualizacion de empleo realizada con exito");
		} catch (Exception e) {
			atributes.addFlashAttribute("mensagemError", "Debe llenar todos los campos");
			return new ModelAndView("redirect:/candidatos/update/{codigoCandidato}");

		}
		return new ModelAndView("redirect:/candidatos/home");

	}

}
