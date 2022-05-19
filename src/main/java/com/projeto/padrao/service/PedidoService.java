package com.projeto.padrao.service;

import com.projeto.padrao.dto.PedidoDTO;
import com.projeto.padrao.enums.EnumAtivoInativo;
import com.projeto.padrao.enums.EnumStatusPedido;
import com.projeto.padrao.enums.EnumTipoConsulta;
import com.projeto.padrao.exceptions.DefaultExceptionHandler;
import com.projeto.padrao.model.Paciente;
import com.projeto.padrao.model.Pedido;
import com.projeto.padrao.repository.PacienteRepository;
import com.projeto.padrao.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class PedidoService extends BaseService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public Pedido cadastrar(PedidoDTO pedidoDTO) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(pedidoDTO)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O objeto tem que estar preenchido.");
        }
        this.validarPedido(pedidoDTO);
        try {
            Paciente paciente = pacienteRepository.findById(pedidoDTO.getPaciente().getId()).get();
            if (paciente == null) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                        "Operação inválida. O id do paciente informado não existe.");
            }
            if (paciente.getAtivoInativo() == "I" || paciente.getAtivoInativo() == null) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                        "Operação inválida. O paciente INATIVO não pode marcar consulta.");
            }
            pedidoDTO.setId(null);
            pedidoDTO.getPostoSaude();
            if (StringUtils.isEmpty(pedidoDTO.getStatusPedido())) {
                pedidoDTO.setStatusPedido(EnumStatusPedido.ABERTO.getCodigo());
            } else {
                pedidoDTO.setStatusPedido(pedidoDTO.getStatusPedido());
            }
            if (StringUtils.isEmpty(pedidoDTO.getTipoConsulta())) {
                pedidoDTO.setTipoConsulta(EnumTipoConsulta.GERAL.getCodigo());
            } else {
                pedidoDTO.setTipoConsulta(pedidoDTO.getTipoConsulta());
            }
            if (StringUtils.isEmpty(pedidoDTO.getDataPedido())) {
                pedidoDTO.setDataPedido(new Date());
            }
            Pedido pedido = convertToModel(pedidoDTO, Pedido.class);
            pedido.setPaciente(paciente);

            return this.pedidoRepository.save(pedido);
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }

    private void validarPedido(PedidoDTO request) throws DefaultExceptionHandler{
        if(StringUtils.isEmpty(request.getPostoSaude().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O posto de saúde não pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getTipoConsulta().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O tipo de consulta não pode ser nulo.");
        }
    }

    public List<Pedido> listarTodos() throws DefaultExceptionHandler {
        List<Pedido> pedidoList = pedidoRepository.findAll();
        if (!pedidoList.isEmpty() || pedidoList != null) {
            try {
                return pedidoList;
            } catch (Exception e) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! Erro leitura pedido.");
            }
        } else {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! Lista vazia.");
        }
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public void deletarPorId(final Integer id) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(id)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! O campo ID é obrigatório.");
        }
        try {
            // tendo ID informado, faz a pesquisa no repositório
            // SE tiver dados, Paciente paciente recebe...
            // SENÃO cai na DefaultExceptionHandler (ternário)
            Pedido pedido = this.pedidoRepository.findById(id).orElseThrow(
                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informação encontrada para os parâmetros informados.")
            );
            // inativar - NAO DELETA DO BANCO, APENAS INATIVA PACIENTE
//            pedido.setAtivoInativo(EnumAtivoInativo.INATIVO.getCodigo());
//            this.pedidoRepository.save(pedido);
            // deletar - DELETA DO BANCO - comentar linha acima e descomentar linha abaixo
            this.pedidoRepository.delete(pedido);
        } catch (DefaultExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }
}
