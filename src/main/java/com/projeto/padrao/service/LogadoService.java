package com.projeto.padrao.service;

import com.projeto.padrao.exceptions.DefaultExceptionHandler;
import com.projeto.padrao.model.Logado;
import com.projeto.padrao.model.Pedido;
import com.projeto.padrao.repository.LogadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
public class LogadoService {

    @Autowired
    LogadoRepository logadoRepository;

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public Logado cadastrar(Integer logadoId) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(logadoId)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O objeto tem que estar preenchido.");
        }
        Logado logado = new Logado();
        logado.setId(logadoId);
        return this.logadoRepository.save(logado);
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public void deslogarPorId(final Integer id) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(id)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! O campo ID é obrigatório.");
        }
        try {
            // tendo ID informado, faz a pesquisa no repositório
            // SE tiver dados, Paciente paciente recebe...
            // SENÃO cai na DefaultExceptionHandler (ternário)
            Logado logado = this.logadoRepository.findById(id).orElseThrow(
                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informação encontrada para os parâmetros informados.")
            );
            // deletar - DELETA DO BANCO
            this.logadoRepository.delete(logado);
        } catch (DefaultExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }
}
