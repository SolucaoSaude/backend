package com.projeto.padrao.model;

import com.projeto.padrao.model.comum.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente extends BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    // DADOS PESSOAIS
    @Column(length = 150)
    private String nome;
    @Column(length = 11)
    private String cpf;
    @Column(name = "cartao_sus", length = 15)
    private String cartaoSus;
    @Column(length = 11)
    private String telefone;
    @Column(name = "data_nascimento")
    private Date dataNascimento;
    @Column(length = 1) // ENUM para genero
    private String genero;

    // DADOS LOGIN
    @Column(length = 50)
    private String username;
    @Column(length = 100)
    private String email;
    @Column(length = 50)
    private String senha;
    @Column(name = "ativo_inativo", length = 1) // ENUM para ativoInativo
    private String ativoInativo;

}
