package com.projeto.padrao.enums;

import lombok.Getter;

public enum EnumTipoAcessoPerfil {

    ADM (1, "ADM"),
    PACIENTE (2, "PACIENTE"),
    FUNCIONARIO (3, "FUNCIONARIO");


    @Getter
    private Integer codigo;

    @Getter
    private String descricao;

    private EnumTipoAcessoPerfil(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
