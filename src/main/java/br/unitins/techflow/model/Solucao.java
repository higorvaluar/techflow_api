package br.unitins.techflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "solucoes")
public class Solucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solucao")
    private Long id;

    @NotBlank(message = "A descrição da solução é obrigatória")
    @Column(name = "descricao_solucao", nullable = false, columnDefinition = "TEXT")
    private String descricaoSolucao;

    @Column(name = "resumo_ia", columnDefinition = "TEXT")
    private String resumoIa;

    @Column(name = "data_solucao", nullable = false)
    private LocalDate dataSolucao;

    @NotNull(message = "O problema é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_problema", nullable = false)
    private Problema problema;

    public Solucao() {
    }

    public Solucao(Long id, String descricaoSolucao, String resumoIa, LocalDate dataSolucao, Problema problema) {
        this.id = id;
        this.descricaoSolucao = descricaoSolucao;
        this.resumoIa = resumoIa;
        this.dataSolucao = dataSolucao;
        this.problema = problema;
    }

    @PrePersist
    public void prePersist() {
        if (dataSolucao == null) {
            dataSolucao = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getDescricaoSolucao() {
        return descricaoSolucao;
    }

    public String getResumoIa() {
        return resumoIa;
    }

    public LocalDate getDataSolucao() {
        return dataSolucao;
    }

    public Problema getProblema() {
        return problema;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescricaoSolucao(String descricaoSolucao) {
        this.descricaoSolucao = descricaoSolucao;
    }

    public void setResumoIa(String resumoIa) {
        this.resumoIa = resumoIa;
    }

    public void setDataSolucao(LocalDate dataSolucao) {
        this.dataSolucao = dataSolucao;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }
}