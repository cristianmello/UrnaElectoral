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
@Table(name = "SegundoTurno")
public class SegundoTurno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigoTurno2;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "CodigoCandidato")
    private Candidatos candidato;

    private int cantidadVotos;

    public int getCodigoTurno2() {
        return codigoTurno2;
    }

    public void setCodigoTurno2(int codigoTurno2) {
        this.codigoTurno2 = codigoTurno2;
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
