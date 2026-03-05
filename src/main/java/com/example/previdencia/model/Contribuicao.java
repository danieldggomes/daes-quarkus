package com.example.previdencia.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "contribuicao")
public class Contribuicao extends PanacheEntity {

    @NotBlank
    @Column(length = 7, nullable = false)
    public String competencia;

    @NotNull
    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contribuicao", nullable = false)
    public TipoContribuicao tipoContribuicao;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cidadao_id", nullable = false)
    public Cidadao cidadao;
}
