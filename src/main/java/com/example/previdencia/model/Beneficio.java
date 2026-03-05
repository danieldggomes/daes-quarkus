package com.example.previdencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "beneficio")
public class Beneficio extends PanacheEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_beneficio", nullable = false)
    public TipoBeneficio tipoBeneficio;

    @Column(name = "data_concessao")
    public LocalDate dataConcessao;

    @NotNull
    @Column(name = "valor_mensal", precision = 15, scale = 2, nullable = false)
    public BigDecimal valorMensal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public StatusBeneficio status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cidadao_id", nullable = false)
    public Cidadao cidadao;

    @JsonIgnore
    @OneToMany(mappedBy = "beneficio", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PagamentoBeneficio> pagamentos = new ArrayList<>();
}
