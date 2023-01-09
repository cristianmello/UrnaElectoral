package com.urnaelectoral.urnaelectoral.Controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.urnaelectoral.urnaelectoral.Repository.CandidatosRepository;
import com.urnaelectoral.urnaelectoral.Repository.PrimerTurnoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/primerTurno")
public class primerTurnoController {

    @Autowired
    private PrimerTurnoRepository primerTurnoRepository;

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
        ModelAndView mv = new ModelAndView("eleccionesPrimerTurno");

        List<Candidatos> candidatos = candidatosRepository.findAll();
        List<PrimerTurno> primerTurno = primerTurnoRepository.findAll();

        mv.addObject("candidatos", candidatos);
        mv.addObject("primerTurno", primerTurno);

        return mv;
    }

    @PostMapping("/elecciones")
    public ModelAndView postElecciones(@Valid PrimerTurno primerTurno, BindingResult result,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagemError", "Verifique los campos obligatorios");
            return new ModelAndView("redirect:/primerTurno/elecciones");
        }

        attributes.addFlashAttribute("mensagemRecibida", "Votoenviado con succeso");

        Candidatos candidato = candidatosRepository.findById(primerTurno.getCandidato().getCodigoCandidato())
                .orElse(null);
        candidato.setCantidadVotos(candidato.getCantidadVotos() + 1);

        primerTurno.setCantidadVotos(primerTurno.getCandidato().getCantidadVotos());

        primerTurnoRepository.save(primerTurno);

        return new ModelAndView("redirect:/primerTurno/elecciones");
    }

    @GetMapping("/resultados")
    public ModelAndView getResultados() {
        ModelAndView mv = new ModelAndView("resultadosPrimerTurno");
        return mv;
    }

    @GetMapping("/mostrarPresidentes")
    public ModelAndView getPresidentes() {

        ModelAndView mv = new ModelAndView("resultadosPrimerTurno");

        List<Candidatos> listaCandidatos = candidatosRepository.findAll();
        List<Candidatos> listaPresidentes = new ArrayList<Candidatos>();

        int cantidadVotosPresidentes = 0;
        double votosPorcentaje = 0.0;

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getTipoCargo().equalsIgnoreCase("Presidente")) {
                cantidadVotosPresidentes += candidato.getCantidadVotos();
                listaPresidentes.add(candidato);
            }
        }

        if (cantidadVotosPresidentes > 0) {
            for (Candidatos presidente : listaPresidentes) {

                DecimalFormat formato = new DecimalFormat("#.00");
                votosPorcentaje = (presidente.getCantidadVotos()) * 1.0 / cantidadVotosPresidentes * 100;

                String votosString = formato.format(votosPorcentaje);
                votosString = votosString.replaceAll(",", ".");
                double doublePorcentaje = Double.parseDouble(votosString);

                presidente.setPorcentajeVotos(doublePorcentaje);
                candidatosRepository.save(presidente);
            }
        }

        mv.addObject("candidatos", listaPresidentes);

        return mv;
    }

    @GetMapping("/mostrarGobernadores")
    public ModelAndView getGobernadores() {

        ModelAndView mv = new ModelAndView("resultadosPrimerTurno");

        List<Candidatos> listaCandidatos = candidatosRepository.findAll();
        List<Candidatos> listaGobernadores = new ArrayList<Candidatos>();

        int cantidadVotosGobernadores = 0;
        double votosPorcentaje = 0;

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getTipoCargo().equalsIgnoreCase("Gobernador")) {
                cantidadVotosGobernadores += candidato.getCantidadVotos();
                listaGobernadores.add(candidato);
            }
        }

        if (cantidadVotosGobernadores > 0) {
            for (Candidatos gobernador : listaGobernadores) {

                DecimalFormat formato = new DecimalFormat("#.00");
                votosPorcentaje = (gobernador.getCantidadVotos()) * 1.0 / cantidadVotosGobernadores * 100;

                String votosString = formato.format(votosPorcentaje);
                votosString = votosString.replaceAll(",", ".");
                double doublePorcentaje = Double.parseDouble(votosString);

                gobernador.setPorcentajeVotos(doublePorcentaje);
                candidatosRepository.save(gobernador);
            }
        }
        mv.addObject("candidatos", listaGobernadores);

        return mv;
    }

    @GetMapping("/mostrarSenadores")
    public ModelAndView getSenadores() {

        ModelAndView mv = new ModelAndView("resultadosPrimerTurno");

        List<Candidatos> listaCandidatos = candidatosRepository.findAll();
        List<Candidatos> listaSenadores = new ArrayList<Candidatos>();

        int cantidadVotosSenadores = 0;
        double votosPorcentaje = 0;

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getTipoCargo().equalsIgnoreCase("Senador")) {
                cantidadVotosSenadores += candidato.getCantidadVotos();
                listaSenadores.add(candidato);
            }
        }

        if (cantidadVotosSenadores > 0) {
            for (Candidatos senador : listaSenadores) {
                DecimalFormat formato = new DecimalFormat("#.00");
                votosPorcentaje = (senador.getCantidadVotos()) * 1.0 / cantidadVotosSenadores * 100;

                String votosString = formato.format(votosPorcentaje);
                votosString = votosString.replaceAll(",", ".");
                double doublePorcentaje = Double.parseDouble(votosString);

                senador.setPorcentajeVotos(doublePorcentaje);
                candidatosRepository.save(senador);
            }
        }

        mv.addObject("candidatos", listaSenadores);

        return mv;
    }

    @GetMapping("/mostrarFederales")
    public ModelAndView getFederales() {

        ModelAndView mv = new ModelAndView("resultadosPrimerTurno");

        List<Candidatos> listaCandidatos = candidatosRepository.findAll();
        List<Candidatos> listaFederales = new ArrayList<Candidatos>();

        int cantidadVotosFederales = 0;
        double votosPorcentaje = 0;

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getTipoCargo().equalsIgnoreCase("Diputado Federal")) {
                cantidadVotosFederales += candidato.getCantidadVotos();
                listaFederales.add(candidato);
            }
        }

        if (cantidadVotosFederales > 0) {
            for (Candidatos federal : listaFederales) {
                DecimalFormat formato = new DecimalFormat("#.00");
                votosPorcentaje = (federal.getCantidadVotos()) * 1.0 / cantidadVotosFederales * 100;

                String votosString = formato.format(votosPorcentaje);
                votosString = votosString.replaceAll(",", ".");
                double doublePorcentaje = Double.parseDouble(votosString);

                federal.setPorcentajeVotos(doublePorcentaje);
                candidatosRepository.save(federal);
            }
        }

        mv.addObject("candidatos", listaFederales);

        return mv;
    }

    @GetMapping("/mostrarEstatales")
    public ModelAndView getEstatales() {

        ModelAndView mv = new ModelAndView("resultadosPrimerTurno");

        List<Candidatos> listaCandidatos = candidatosRepository.findAll();
        List<Candidatos> listaEstatales = new ArrayList<Candidatos>();

        int cantidadVotosEstatales = 0;
        double votosPorcentaje = 0;

        for (Candidatos candidato : listaCandidatos) {
            if (candidato.getTipoCargo().equalsIgnoreCase("Diputado Estatal")) {
                cantidadVotosEstatales += candidato.getCantidadVotos();
                listaEstatales.add(candidato);
            }
        }
        if (cantidadVotosEstatales > 0) {
            for (Candidatos estatal : listaEstatales) {
                DecimalFormat formato = new DecimalFormat("#.00");
                votosPorcentaje = (estatal.getCantidadVotos()) * 1.0 / cantidadVotosEstatales * 100;

                String votosString = formato.format(votosPorcentaje);
                votosString = votosString.replaceAll(",", ".");
                double doublePorcentaje = Double.parseDouble(votosString);

                estatal.setPorcentajeVotos(doublePorcentaje);
                candidatosRepository.save(estatal);
            }
        }
        mv.addObject("candidatos", listaEstatales);

        return mv;
    }

    // @GetMapping("/resultados")
    // public ModelAndView j() {

    // ModelAndView mv = new ModelAndView("resultadosPrimerTurno");

    // List<Candidatos> listaCandidatos = candidatosRepository.findAll();

    // int cantidadVotosPresidentes = 0;
    // int cantidadVotosGobernador = 0;
    // int cantidadVotosSenador = 0;
    // int cantidadVotosFederal = 0;
    // int cantidadVotosEstatal = 0;

    // for (Candidatos candidato : listaCandidatos) {

    // if (candidato.getTipoCargo().equalsIgnoreCase("Presidente")) {
    // cantidadVotosPresidentes += candidato.getCantidadVotos();
    // }
    // if (candidato.getTipoCargo().equalsIgnoreCase("Gobernador")) {
    // cantidadVotosGobernador += candidato.getCantidadVotos();
    // }
    // if (candidato.getTipoCargo().equalsIgnoreCase("Senador")) {
    // cantidadVotosSenador += candidato.getCantidadVotos();
    // }
    // if (candidato.getTipoCargo().equalsIgnoreCase("Federal")) {
    // cantidadVotosFederal += candidato.getCantidadVotos();
    // }
    // if (candidato.getTipoCargo().equalsIgnoreCase("Estatal")) {
    // cantidadVotosEstatal += candidato.getCantidadVotos();
    // }

    // }

    // mv.addObject("candidatos", listaCandidatos);
    // mv.addObject("votosPresidentes", cantidadVotosPresidentes);
    // mv.addObject("votosGobernador", cantidadVotosGobernador);
    // mv.addObject("votonSenador", cantidadVotosSenador);
    // mv.addObject("votosFederal", cantidadVotosFederal);
    // mv.addObject("votosEstatal", cantidadVotosEstatal);

    // return mv;

    // }

    /*
     * @GetMapping("/sortearCandidatos")
     * public ModelAndView sortearCandidatos() {
     * 
     * Candidatos presidenteExistente;
     * Candidatos gobernadorExistente;
     * Candidatos senadorExistente;
     * Candidatos diputadoFederalExistente;
     * Candidatos diputadoEstatalExistente;
     * 
     * ModelAndView mv = new ModelAndView("sorteoPrimerTurno");
     * 
     * List<Candidatos> listaCandidatos = candidatosRepository.findAll();
     * 
     * do {
     * presidenteExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while (!presidenteExistente.getTipoCargo().equalsIgnoreCase("Presidente"));
     * 
     * do {
     * gobernadorExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while (!gobernadorExistente.getTipoCargo().equalsIgnoreCase("Gobernador"));
     * 
     * do {
     * senadorExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while (!senadorExistente.getTipoCargo().equalsIgnoreCase("Senador"));
     * 
     * do {
     * diputadoFederalExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while
     * (!diputadoFederalExistente.getTipoCargo().equalsIgnoreCase("Diputado Federal"
     * ));
     * 
     * do {
     * diputadoEstatalExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while
     * (!diputadoEstatalExistente.getTipoCargo().equalsIgnoreCase("Diputado Estatal"
     * ));
     * 
     * mv.addObject("presidente", presidenteExistente);
     * mv.addObject("gobernador", gobernadorExistente);
     * mv.addObject("senador", senadorExistente);
     * mv.addObject("diputadoFederal", diputadoFederalExistente);
     * mv.addObject("diputadoEstatal", diputadoEstatalExistente);
     * 
     * return mv;
     * }
     * 
     * 
     * @GetMapping("/sortearCandidatos/gobernador")
     * public ModelAndView sortearGobernador() {
     * 
     * Candidatos candidatoExistente;
     * 
     * ModelAndView mv = new ModelAndView("elecciones");
     * 
     * List<Candidatos> listaCandidatos = candidatosRepository.findAll();
     * 
     * do {
     * candidatoExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while (candidatoExistente.getTipoCargo().equalsIgnoreCase("Gobernador"));
     * 
     * mv.addObject("nombreCandidato", candidatoExistente.getNombreCandidato());
     * 
     * return mv;
     * }
     * 
     * @GetMapping("/sortearCandidatos/senador")
     * public ModelAndView sortearSenador() {
     * 
     * Candidatos candidatoExistente;
     * 
     * ModelAndView mv = new ModelAndView("elecciones");
     * 
     * List<Candidatos> listaCandidatos = candidatosRepository.findAll();
     * 
     * do {
     * candidatoExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while (candidatoExistente.getTipoCargo().equalsIgnoreCase("Senador"));
     * 
     * mv.addObject("nombreCandidato", candidatoExistente.getNombreCandidato());
     * 
     * return mv;
     * }
     * 
     * @GetMapping("/sortearCandidatos/diputadoFederal")
     * public ModelAndView sortearDiputadoFederal() {
     * 
     * Candidatos candidatoExistente;
     * 
     * ModelAndView mv = new ModelAndView("elecciones");
     * 
     * List<Candidatos> listaCandidatos = candidatosRepository.findAll();
     * 
     * do {
     * candidatoExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while
     * (candidatoExistente.getTipoCargo().equalsIgnoreCase("Diputado Federal"));
     * 
     * mv.addObject("nombreCandidato", candidatoExistente.getNombreCandidato());
     * 
     * return mv;
     * }
     * 
     * @GetMapping("/sortearCandidatos/diputadoEstatal")
     * public ModelAndView sortearDiputadoEstatal() {
     * 
     * Candidatos candidatoExistente;
     * 
     * ModelAndView mv = new ModelAndView("elecciones");
     * 
     * List<Candidatos> listaCandidatos = candidatosRepository.findAll();
     * 
     * do {
     * candidatoExistente = candidatosRepository
     * .findById(listaCandidatos.get((int) (Math.random() *
     * listaCandidatos.size())).getCodigoCandidato())
     * .orElse(null);
     * } while
     * (candidatoExistente.getTipoCargo().equalsIgnoreCase("Diputado Estatal"));
     * 
     * mv.addObject("nombreCandidato", candidatoExistente.getNombreCandidato());
     * 
     * return mv;
     * }
     *
     *
     * @RequestMapping(value = "/update/{codigoCandidato}", method =
     * RequestMethod.POST)
     * public String updateEmpleo(Candidatos candidatos, BindingResult result,
     * RedirectAttributes atributes) {
     * 
     * Candidatos candidatoExistente =
     * candidatoRepository.findById(candidatos.getCodigoCandidato()).orElse(null);
     * 
     * candidatoExistente.setNombreCandidato(candidatos.getNombreCandidato());
     * candidatoExistente.setTipoCargo(candidatos.getTipoCargo());
     * 
     * try {
     * candidatoRepository.save(candidatoExistente);
     * atributes.addFlashAttribute("mensagemRecibida",
     * "Actualizacion de empleo realizada con exito");
     * } catch (Exception e) {
     * atributes.addFlashAttribute("mensagemError", "Debe llenar todos los campos");
     * return "redirect:/candidato/update/{codigoCandidato}";
     * }
     * return "redirect:/home/";
     * 
     * }
     * 
     * 
     * 
     * 
     * @GetMapping("/sortearCandidatos")
     * public String sortearCandidatos() {
     * List<Candidatos> listCantidatos = candidatosRepository.findAll();
     * listCantidatos.get()
     * 
     * while (listCantidatos.size() > 0) {
     * int indexC = (int) Math.floor(Math.random()*listCantidatos.size());
     * 
     * }
     * return null;
     * 
     * }
     */
}
