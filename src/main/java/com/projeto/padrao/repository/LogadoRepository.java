package com.projeto.padrao.repository;

import com.projeto.padrao.model.Logado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogadoRepository extends JpaRepository<Logado, Integer> {
}
