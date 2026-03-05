package com.example.previdencia.testsupport;

import com.example.previdencia.model.Beneficio;
import com.example.previdencia.model.Cidadao;
import com.example.previdencia.model.Contribuicao;
import com.example.previdencia.model.PagamentoBeneficio;
import com.example.previdencia.model.Sexo;
import com.example.previdencia.model.StatusBeneficio;
import com.example.previdencia.model.TipoBeneficio;
import com.example.previdencia.model.TipoContribuicao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public final class TestDataFactory {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private TestDataFactory() {
    }

    public static Cidadao cidadao() {
        int suffix = COUNTER.getAndIncrement();
        Cidadao c = new Cidadao();
        c.cpf = String.format("%011d", suffix);
        c.nome = "Cidadao Teste " + suffix;
        c.dataNascimento = LocalDate.of(1980, 1, 1);
        c.sexo = Sexo.MASCULINO;
        c.dataCadastro = LocalDate.now();
        return c;
    }

    public static Beneficio beneficio(Cidadao cidadao) {
        Beneficio b = new Beneficio();
        b.tipoBeneficio = TipoBeneficio.APOSENTADORIA_IDADE;
        b.dataConcessao = LocalDate.now().minusYears(1);
        b.valorMensal = new BigDecimal("1200.00");
        b.status = StatusBeneficio.ATIVO;
        b.cidadao = cidadao;
        return b;
    }

    public static Contribuicao contribuicao(Cidadao cidadao) {
        Contribuicao c = new Contribuicao();
        c.competencia = "2026-01";
        c.valor = new BigDecimal("450.00");
        c.tipoContribuicao = TipoContribuicao.EMPREGADO;
        c.cidadao = cidadao;
        return c;
    }

    public static PagamentoBeneficio pagamento(Beneficio beneficio) {
        PagamentoBeneficio p = new PagamentoBeneficio();
        p.competencia = "2026-01";
        p.valorPago = new BigDecimal("1200.00");
        p.dataPagamento = LocalDate.now();
        p.beneficio = beneficio;
        return p;
    }
}
