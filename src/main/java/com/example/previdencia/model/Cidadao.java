package com.example.previdencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cidadao")
public class Cidadao extends PanacheEntity {

    @NotBlank
    @Size(min = 11, max = 11)
    @Column(unique = true, length = 11, nullable = false)
    public String cpf;

    @NotBlank
    @Column(nullable = false)
    public String nome;

    @NotNull
    @Past
    @Column(name = "data_nascimento", nullable = false)
    public LocalDate dataNascimento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Sexo sexo;

    @NotNull
    @Column(name = "data_cadastro", nullable = false)
    public LocalDate dataCadastro;

    @JsonIgnore
    @OneToMany(mappedBy = "cidadao", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Beneficio> beneficios = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "cidadao", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Contribuicao> contribuicoes = new ArrayList<>();
}
