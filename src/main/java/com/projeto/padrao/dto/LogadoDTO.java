package com.projeto.padrao.dto;

import com.projeto.padrao.model.Paciente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogadoDTO {

    private Integer id;
}
