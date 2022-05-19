package com.projeto.padrao.dto;

import com.projeto.padrao.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {

    private Integer id;

    private String nome;
    private String cpf;
    private String cartaoSus;
    private String telefone;
    private Date dataNascimento;
    private String genero;

    private String username;
    private String email;
    private String senha;
    private String ativoInativo;

    public String getDataNascimentoFormatada() {
        if (dataNascimento != null) {
            return DateTimeUtils.formataData(dataNascimento, "dd/MM/yyyy");
        } else {
            return "";
        }
    }

}
