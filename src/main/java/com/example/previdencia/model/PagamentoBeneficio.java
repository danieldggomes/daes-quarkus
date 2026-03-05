package com.example.previdencia.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagamento_beneficio")
public class PagamentoBeneficio extends PanacheEntity {

    @NotBlank
    @Column(length = 7, nullable = false)
    public String competencia;

    @NotNull
    @Column(name = "valor_pago", precision = 15, scale = 2, nullable = false)
    public BigDecimal valorPago;

    @NotNull
    @Column(name = "data_pagamento", nullable = false)
    public LocalDate dataPagamento;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "beneficio_id", nullable = false)
    public Beneficio beneficio;
}
