package com.projeto.padrao.controller;

import com.projeto.padrao.exceptions.DefaultExceptionHandler;
import com.projeto.padrao.model.Logado;
import com.projeto.padrao.repository.LogadoRepository;
import com.projeto.padrao.service.LogadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/api/logado")
public class LogadoController {

    @Autowired
    LogadoService logadoService;

    @Autowired
    LogadoRepository logadoRepository;

//    @PostMapping("/cadastrar")
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public ResponseEntity<Logado> cadastrar(@Valid @RequestBody Logado logado) throws DefaultExceptionHandler {
//        this.logadoService.cadastrar(logado);
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(logado.getId()).toUri();
//        return ResponseEntity.created(location).body(logado);
//    }

    @GetMapping("/listar-logado")
    public ResponseEntity<List<Logado>> listarLogado() throws DefaultExceptionHandler {
        List<Logado> logadoList = this.logadoRepository.findAll();
        if (!logadoList.isEmpty() || logadoList != null) {
            try {
                return ResponseEntity.ok(logadoList);
            } catch (Exception e) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! Erro leitura paciente.");
            }
        } else {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! Lista vazia.");
        }
    }

    @DeleteMapping("/deslogar/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deslogarPorId(@Valid @PathVariable("id") Integer id) throws DefaultExceptionHandler {
        this.logadoService.deslogarPorId(id);
    }
}
