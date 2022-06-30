package com.projeto.padrao.repository;

import com.projeto.padrao.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("FROM Pedido p WHERE p.postoSaude like %:keyword% OR p.tipoConsulta like %:keyword% OR p.paciente.nome like %:keyword% OR p.paciente.cpf like %:keyword%")
    Page<Pedido> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("FROM Pedido p WHERE p.id = ?1")
    Pedido buscarPedido(Integer id);

    @Query("FROM Pedido p WHERE p.paciente.id = ?1")
    List<Pedido> findAllById(Integer id);


}
