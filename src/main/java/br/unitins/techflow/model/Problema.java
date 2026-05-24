package br.unitins.techflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "problemas")
public class Problema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_problema")
    private Long id;

    @NotBlank(message = "O título do problema é obrigatório")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "A descrição do problema é obrigatória")
    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @NotNull(message = "A categoria é obrigatória")
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @NotNull(message = "O técnico é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Tecnico tecnico;

    public Problema() {
    }

    public Problema(Long id, String titulo, String descricao, LocalDate dataRegistro, Categoria categoria, Tecnico tecnico) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataRegistro = dataRegistro;
        this.categoria = categoria;
        this.tecnico = tecnico;
    }

    @PrePersist
    public void prePersist() {
        if (dataRegistro == null) {
            dataRegistro = LocalDate.now();
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

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Tecnico getTecnico() {
        return tecnico;
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

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
}