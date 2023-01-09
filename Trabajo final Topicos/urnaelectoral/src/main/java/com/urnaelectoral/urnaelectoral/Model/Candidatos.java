package com.urnaelectoral.urnaelectoral.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Candidatos")
public class Candidatos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigoCandidato;

	private String nombreCandidato;

	private String tipoCargo;

	private int cantidadVotos;

	private double porcentajeVotos;

	public double getPorcentajeVotos() {
		return porcentajeVotos;
	}

	public void setPorcentajeVotos(double porcentajeVotos) {
		this.porcentajeVotos = porcentajeVotos;
	}

	public int getCantidadVotos() {
		return cantidadVotos;
	}

	public void setCantidadVotos(int cantidadVotos) {
		this.cantidadVotos = cantidadVotos;
	}

	public String getTipoCargo() {
		return tipoCargo;
	}

	public void setTipoCargo(String tipoCargo) {
		this.tipoCargo = tipoCargo;
	}

	public int getCodigoCandidato() {
		return codigoCandidato;
	}

	public void setCodigoCandidato(int codigoCandidato) {
		this.codigoCandidato = codigoCandidato;
	}

	public String getNombreCandidato() {
		return nombreCandidato;
	}

	public void setNombreCandidato(String nombreCandidato) {
		this.nombreCandidato = nombreCandidato;
	}

}