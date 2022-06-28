package com.projeto.padrao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logados")
public class Logado {
    @Id
    @EqualsAndHashCode.Include
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

//    @OneToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name = "paciente_id")
//    private Paciente paciente;
}
