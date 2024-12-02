package br.anhembi.heal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long codigo; // Código gerado automaticamente pelo banco de dados

    // Relacionamento com o paciente
    @ManyToOne
    @JoinColumn(name = "paciente_rg", nullable = false)
    private Paciente paciente; // Relacionamento com o paciente

    // Relacionamento com o médico
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico; // Relacionamento com o médico

    @NotBlank
    private String nomeConsulta;

    @NotNull
    private LocalDate dataConsulta; // Data da consulta

    @NotNull
    private LocalTime horaConsulta; // Horário da consulta

    // Getters e setters
    public long getCodigo() {
        return codigo; // Retorna o código gerado automaticamente
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    // Como o nome da clínica é fixo, não fornecemos um setter para nomeClinica

    public Paciente getPaciente() {
        return paciente; // Retorna o paciente associado à consulta
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente; // Define o paciente associado à consulta
    }

    public Medico getMedico() {
        return medico; // Retorna o médico associado à consulta
    }

    public void setMedico(Medico medico) {
        this.medico = medico; // Define o médico associado à consulta
    }

    public String getNomeConsulta() {
        return nomeConsulta;
    }

    public void setNomeConsulta(String nomeConsulta) {
        this.nomeConsulta = nomeConsulta;
    }

    public LocalDate getDataConsulta() {
        return dataConsulta; // Retorna a data da consulta
    }

    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta; // Define a data da consulta
    }

    public LocalTime getHoraConsulta() {
        return horaConsulta; // Retorna o horário da consulta
    }

    public void setHoraConsulta(LocalTime horaConsulta) {
        this.horaConsulta = horaConsulta; // Define o horário da consulta
    }
}