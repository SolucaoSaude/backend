package com.projeto.padrao.controller;

import com.projeto.padrao.dto.PacienteDTO;
import com.projeto.padrao.dto.PageDTO;
import com.projeto.padrao.exceptions.DefaultExceptionHandler;
import com.projeto.padrao.model.Logado;
import com.projeto.padrao.model.Paciente;
import com.projeto.padrao.repository.LogadoRepository;
import com.projeto.padrao.repository.PacienteRepository;
import com.projeto.padrao.service.LogadoService;
import com.projeto.padrao.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/api/paciente")
public class PacienteController extends BaseController {

    @Autowired
    PacienteService pacienteService;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    LogadoService logadoService;

    @PostMapping("/cadastrar")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PacienteDTO> cadastrar(@Valid @RequestBody PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        Paciente paciente = this.pacienteService.cadastrar(pacienteDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(location).body(super.convertTo(paciente, PacienteDTO.class));
    }
    
    @GetMapping("/listar-todos")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<Paciente>> listarTodos() throws DefaultExceptionHandler {
        List<Paciente> pacienteList = this.pacienteService.listarTodos();
        return ResponseEntity.ok(pacienteList);
    }

    @GetMapping("/listar/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Paciente> listarPorId(@PathVariable(value = "id")Integer id) throws DefaultExceptionHandler {
        Paciente paciente = this.pacienteService.listarPorId(id);
        return ResponseEntity.ok(paciente);
    }

    //@RequestMapping(value = "/consultar-paginado", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PostMapping("/consultar-paginado")
    public ResponseEntity<PageDTO<PacienteDTO>> consultarPaginado(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                               @RequestBody(required = false) PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        PageDTO<PacienteDTO> out = this.pacienteService.consultarPaginado(page, size, pacienteDTO);
        return ResponseEntity.ok(out);
    }

    @PutMapping("/atualizar")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<PacienteDTO> atualizar(@Valid @RequestBody PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        Paciente paciente = this.pacienteService.atualizar(pacienteDTO);
        return ResponseEntity.ok(super.convertTo(paciente, PacienteDTO.class));
    }

    @PutMapping("/ativar/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void ativarPorId(@Valid @PathVariable("id") Integer id) throws DefaultExceptionHandler {
        this.pacienteService.ativarPorId(id);
    }

    @DeleteMapping("/deletar/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletarPorId(@Valid @PathVariable("id") Integer id) throws DefaultExceptionHandler {
        this.pacienteService.deletarPorId(id);
    }

    @PostMapping("/logar")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<PacienteDTO> logarPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        String tempEmail = pacienteDTO.getEmail();
        String tempSenha = pacienteDTO.getSenha();
        Paciente paciente = null;

        if (tempEmail != null && tempSenha != null) {
            paciente = pacienteService.consultarPorEmailSenha(tempEmail, tempSenha);
            paciente.getId();

            Integer logadoId = paciente.getId();
            this.logadoService.cadastrar(logadoId);

            if (ObjectUtils.isEmpty(paciente)) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                        "Login inválido! Email ou senha não cadastrados ou incorretos.");
            }
        }
        return ResponseEntity.accepted().body(super.convertTo(paciente, PacienteDTO.class));
    }

}
