package com.urnaelectoral.urnaelectoral.Controller;

import java.util.ArrayList;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.urnaelectoral.urnaelectoral.Model.Candidatos;
import com.urnaelectoral.urnaelectoral.Model.PrimerTurno;
import com.urnaelectoral.urnaelectoral.Model.SegundoTurno;
import com.urnaelectoral.urnaelectoral.Repository.CandidatosRepository;
import com.urnaelectoral.urnaelectoral.Repository.PrimerTurnoRepository;
import com.urnaelectoral.urnaelectoral.Repository.SegundoTurnoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/segundoTurno")
public class segundoTurnoController {

    @Autowired
    private PrimerTurnoRepository primerTurnoRepository;

    @Autowired
    private SegundoTurnoRepository segundoTurnoRepository;

    @Autowired
    private CandidatosRepository candidatosRepository;

    // Post
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PrimerTurno candidatoPrimerTurno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(primerTurnoRepository.save(candidatoPrimerTurno));
    }

    // Get
    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable int id) {
        Optional<PrimerTurno> oCandidatoPrimerTurno = primerTurnoRepository.findById(id);

        if (!oCandidatoPrimerTurno.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oCandidatoPrimerTurno);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody PrimerTurno primerTurnoDetalles,
            @PathVariable(value = "id") int codigoPrimerTurno) {
        Optional<PrimerTurno> oCandidatoPrimerTurno = primerTurnoRepository.findById(codigoPrimerTurno);

        if (!oCandidatoPrimerTurno.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        oCandidatoPrimerTurno.get().setCandidato(oCandidatoPrimerTurno.get().getCandidato());
        return ResponseEntity.status(HttpStatus.CREATED).body(primerTurnoRepository.save(oCandidatoPrimerTurno.get()));

    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") int primerTurnoId) {

        if (!primerTurnoRepository.findById(primerTurnoId).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        primerTurnoRepository.deleteById(primerTurnoId);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/elecciones")
    public ModelAndView getElecciones() {
        ModelAndView mv = new ModelAndView("eleccionesSegundoTurno");

        ModelAndView mv2 = new ModelAndView("resultadosCandidatos");

        List<Candidatos> listaCandidatos = candidatosRepository.findAll();
        List<Candidatos> listaPresidentes = new ArrayList<Candidatos>();
        List<Candidatos> listaGobernadores = new ArrayList<Candidatos>();
        List<Candidatos> listaSenadores = new ArrayList<Candidatos>();
        List<Candidatos> listaFederales = new ArrayList<Candidatos>();
        List<Candidatos> listaEstatales = new ArrayList<Candidatos>();

        List<Candidatos> nuevaListaPresidentes = new ArrayList<Candidatos>();
        List<Candidatos> nuevaListaGobernadores = new ArrayList<Candidatos>();

        Candidatos primerPresidente;
        Candidatos segundoPresidente;
        Candidatos primerGobernador;
        Candidatos segundoGobernador;

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getTipoCargo().equalsIgnoreCase("Senador")) {
                listaSenadores.add(candidato);
            }
        }

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getTipoCargo().equalsIgnoreCase("Diputado Federal")) {
                listaFederales.add(candidato);
            }
        }

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getTipoCargo().equalsIgnoreCase("Diputado Estatal")) {
                listaEstatales.add(candidato);
            }
        }

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getPorcentajeVotos() > 50 && candidato.getTipoCargo().equalsIgnoreCase("Presidente")) {
                for (Candidatos candidato2 : listaCandidatos) {
                    if (candidato2.getPorcentajeVotos() >= 50
                            && candidato2.getTipoCargo().equalsIgnoreCase("Gobernador")) {
                        mv2.addObject("presidenteElecto", candidato);
                        mv2.addObject("gobernadorElecto", candidato2);
                        mv2.addObject("senadoresElectos", listaSenadores);
                        mv2.addObject("federalesElectos", listaFederales);
                        mv2.addObject("estatalesElectos", listaEstatales);

                        return mv2;
                    }
                }
            }
        }

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getPorcentajeVotos() <= 50 && candidato.getTipoCargo().equalsIgnoreCase("Presidente")) {
                listaPresidentes.add(candidato);
            }
        }

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getPorcentajeVotos() <= 50 && candidato.getTipoCargo().equalsIgnoreCase("Gobernador")) {
                listaGobernadores.add(candidato);
            }
        }

        if (listaPresidentes.size() == 2) {
            if (listaGobernadores.size() == 0 || listaGobernadores.size() == 1) {
                mv.addObject("presidentes", listaPresidentes);

                return mv;
            } else if (listaGobernadores.size() == 2) {
                mv.addObject("presidentes", listaPresidentes);
                mv.addObject("gobernadores", listaGobernadores);

                return mv;

            } else if (listaGobernadores.size() > 2) {

                primerGobernador = listaGobernadores.stream()
                        .max(Comparator.comparingDouble(Candidatos::getPorcentajeVotos)).get();

                listaGobernadores.remove(primerGobernador);

                segundoGobernador = listaGobernadores.stream()
                        .max(Comparator.comparingDouble(Candidatos::getPorcentajeVotos)).get();

                nuevaListaGobernadores.add(primerGobernador);
                nuevaListaGobernadores.add(segundoGobernador);

                mv.addObject("presidentes", nuevaListaPresidentes);
                mv.addObject("gobernadores", nuevaListaGobernadores);

                return mv;
            }

        }

        if (listaPresidentes.size() > 2) {

            primerPresidente = listaPresidentes.stream()
                    .max(Comparator.comparingDouble(Candidatos::getPorcentajeVotos)).get();

            listaPresidentes.remove(primerPresidente);

            segundoPresidente = listaPresidentes.stream()
                    .max(Comparator.comparingDouble(Candidatos::getPorcentajeVotos)).get();

            nuevaListaPresidentes.add(primerPresidente);
            nuevaListaPresidentes.add(segundoPresidente);

            if (listaGobernadores.size() == 0 || listaGobernadores.size() == 1) {

                mv.addObject("presidentes", nuevaListaPresidentes);
                mv.addObject("gobernantes", listaGobernadores);

                return mv;

            } else if (listaGobernadores.size() == 2) {

                mv.addObject("presidentes", nuevaListaPresidentes);
                mv.addObject("gobernadores", listaGobernadores);

                return mv;

            } else if (listaGobernadores.size() > 2) {

                primerGobernador = listaGobernadores.stream()
                        .max(Comparator.comparingDouble(Candidatos::getPorcentajeVotos)).get();

                listaGobernadores.remove(primerGobernador);

                segundoGobernador = listaGobernadores.stream()
                        .max(Comparator.comparingDouble(Candidatos::getPorcentajeVotos)).get();

                nuevaListaGobernadores.add(primerGobernador);
                nuevaListaGobernadores.add(segundoGobernador);

                mv.addObject("presidentes", nuevaListaPresidentes);
                mv.addObject("gobernadores", nuevaListaGobernadores);

                return mv;
            }

        }

        return mv;

    }

    @PostMapping("/elecciones")
    public ModelAndView postElecciones(@Valid SegundoTurno segundoTurno, BindingResult result,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagemError", "Verifique los campos obligatorios");
            return new ModelAndView("redirect:/primerTurno/elecciones");
        }

        attributes.addFlashAttribute("mensagemRecibida", "Votoenviado con succeso");

        Candidatos candidato = candidatosRepository.findById(segundoTurno.getCandidato().getCodigoCandidato())
                .orElse(null);
        candidato.setCantidadVotos(candidato.getCantidadVotos() + 1);

        segundoTurno.setCantidadVotos(segundoTurno.getCandidato().getCantidadVotos());

        segundoTurnoRepository.save(segundoTurno);

        return new ModelAndView("redirect:/segundoTurno/elecciones");
    }

    @GetMapping("/eleccionFinalizada")
    public ModelAndView finalizarEleccion() {

        ModelAndView mv = new ModelAndView("home");

        List<Candidatos> candidatos = candidatosRepository.findAll();
        List<PrimerTurno> listaPrimerTurno = primerTurnoRepository.findAll();
        List<SegundoTurno> listaSegundoTurno = segundoTurnoRepository.findAll();

        for (Candidatos candidato : candidatos) {
            candidatosRepository.delete(candidato);
        }

        for (PrimerTurno candidato : listaPrimerTurno) {
            primerTurnoRepository.delete(candidato);
        }

        for (SegundoTurno candidato : listaSegundoTurno) {
            segundoTurnoRepository.delete(candidato);
        }

        return mv;

    }
}
