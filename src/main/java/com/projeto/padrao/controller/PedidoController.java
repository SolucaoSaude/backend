package com.projeto.padrao.controller;

import com.projeto.padrao.dto.PedidoDTO;
import com.projeto.padrao.exceptions.DefaultExceptionHandler;
import com.projeto.padrao.model.Paciente;
import com.projeto.padrao.model.Pedido;
import com.projeto.padrao.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/api/pedido")
public class PedidoController extends BaseController {

    @Autowired
    PedidoService pedidoService;

    @PostMapping("/cadastrar")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PedidoDTO> cadastrar(@Valid @RequestBody PedidoDTO pedidoDTO) throws DefaultExceptionHandler {
        Pedido pedido = this.pedidoService.cadastrar(pedidoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(location).body(super.convertTo(pedido, PedidoDTO.class));
    }

    @GetMapping("/listar-todos")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<Pedido>> listarTodos() throws DefaultExceptionHandler {
        List<Pedido> pedidoList = this.pedidoService.listarTodos();
        return ResponseEntity.ok(pedidoList);
    }

    @DeleteMapping("/deletar/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletarPorId(@Valid @PathVariable("id") Integer id) throws DefaultExceptionHandler {
        this.pedidoService.deletarPorId(id);
    }
}
