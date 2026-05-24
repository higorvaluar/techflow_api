package br.unitins.techflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "chamados")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chamado")
    private Long id;

    @NotBlank(message = "O título do chamado é obrigatório")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "A descrição do chamado é obrigatória")
    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @NotBlank(message = "O status do chamado é obrigatório")
    @Size(max = 50, message = "O status deve ter no máximo 50 caracteres")
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @NotBlank(message = "A prioridade do chamado é obrigatória")
    @Size(max = 50, message = "A prioridade deve ter no máximo 50 caracteres")
    @Column(name = "prioridade", nullable = false, length = 50)
    private String prioridade;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDate dataFechamento;

    @NotNull(message = "O técnico é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Tecnico tecnico;

    @NotNull(message = "O problema é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_problema", nullable = false)
    private Problema problema;

    public Chamado() {
    }

    public Chamado(Long id, String titulo, String descricao, String status, String prioridade,
                   LocalDate dataAbertura, LocalDate dataFechamento, Tecnico tecnico, Problema problema) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.tecnico = tecnico;
        this.problema = problema;
    }

    @PrePersist
    public void prePersist() {
        if (dataAbertura == null) {
            dataAbertura = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getStatus() {
        return status;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public Problema getProblema() {
        return problema;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }
}