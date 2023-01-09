package com.urnaelectoral.urnaelectoral.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PrimerTurno")
public class PrimerTurno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigoTurno1;

    private int cantidadVotos;


    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "codigoCandidato")
    private Candidatos candidato;

    public int getCodigoTurno1() {
        return codigoTurno1;
    }

    public void setCodigoTurno1(int codigoTurno1) {
        this.codigoTurno1 = codigoTurno1;
    }


    public int getCantidadVotos() {
        return cantidadVotos;
    }

    public void setCantidadVotos(int cantidadVotos) {
        this.cantidadVotos = cantidadVotos;
    }

    public Candidatos getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidatos candidato) {
        this.candidato = candidato;
    }

}
