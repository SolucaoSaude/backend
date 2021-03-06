package com.projeto.padrao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Integer id;
    private String postoSaude;
    private String statusPedido;
    private String tipoConsulta;
    private Date dataPedido;
    private Date dataConsulta;

    private PacienteDTO pacienteDTO;

}
