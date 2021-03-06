package com.projeto.padrao.service;

import com.projeto.padrao.dto.PacienteDTO;
import com.projeto.padrao.dto.PageDTO;
import com.projeto.padrao.enums.EnumAtivoInativo;
import com.projeto.padrao.exceptions.DefaultExceptionHandler;
import com.projeto.padrao.model.Logado;
import com.projeto.padrao.model.Paciente;
import com.projeto.padrao.repository.LogadoRepository;
import com.projeto.padrao.repository.PacienteRepository;
import com.projeto.padrao.utils.CpfCnpjUtil;
import com.projeto.padrao.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService extends BaseService {

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private LogadoRepository logadoRepository;


    public Optional<Paciente> consultarPorCpfCartaoSus(final String cpf, final String cartaoSus) {
        return this.pacienteRepository.findByCpfAndCartaoSus(NumberUtil.toNumber(cpf), NumberUtil.toNumber(cartaoSus));
    }

    public Paciente consultarPorEmailSenha(final String email, final String senha) {
        return this.pacienteRepository.findByEmailAndSenha(email, senha);
    }

//    public void saveIdLogado(Integer idLogado) {
//        Integer idSave = new Logado();
//        idSave = idLogado;
//        this.logadoRepository.save(idSave);
//    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public Paciente cadastrar(PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        this.validarPaciente(pacienteDTO);
        try {
            Optional<Paciente> paciente = this.consultarPorCpfCartaoSus(pacienteDTO.getCpf(), pacienteDTO.getCartaoSus());
            if (paciente.isEmpty()) {
                pacienteDTO.setId(null);

                pacienteDTO.setNome(pacienteDTO.getNome());
                pacienteDTO.setCpf(pacienteDTO.getCpf().replaceAll("\\D", ""));
                pacienteDTO.setCartaoSus(pacienteDTO.getCartaoSus().replaceAll("\\D", ""));
                pacienteDTO.setTelefone(pacienteDTO.getTelefone().replaceAll("\\D", ""));
                pacienteDTO.setDataNascimento(pacienteDTO.getDataNascimento());
//                pacienteDTO.setDataNascimento(DateTimeUtils.formataData(pacienteDTO.getDataNascimentoFormatada(),"yyyy-MM-dd"));
                pacienteDTO.setGenero(pacienteDTO.getGenero().substring(0,1).toUpperCase());

                pacienteDTO.setUsername(pacienteDTO.getUsername());
                pacienteDTO.setEmail(pacienteDTO.getEmail());
                pacienteDTO.setSenha(pacienteDTO.getSenha());
                if (StringUtils.isEmpty(pacienteDTO.getAtivoInativo())) {
                    pacienteDTO.setAtivoInativo(EnumAtivoInativo.ATIVO.getCodigo());
                } else {
                    pacienteDTO.setAtivoInativo(pacienteDTO.getAtivoInativo());
                }
            } else {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                        "Opera????o inv??lida! Cpf ou cart??o SUS j?? cadastrados.");
                }
            return this.pacienteRepository.save(super.convertToModel(pacienteDTO, Paciente.class));
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }

    private void validarPaciente(PacienteDTO request) throws DefaultExceptionHandler{
        if(StringUtils.isEmpty(request.getNome().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O nome do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getCpf().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O cpf do paciente n??o pode ser nulo.");
        }
        if (!CpfCnpjUtil.isCPF(request.getCpf())) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O cpf inserido ?? inv??lido.");
        }
        if(StringUtils.isEmpty(request.getCartaoSus().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O cart??o SUS do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getTelefone().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O telefone do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getDataNascimento())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! A data de nascimento do paciente n??o pode ser nula.");
        }
        if(StringUtils.isEmpty(request.getGenero().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O genero do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getUsername().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O username do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getEmail().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O email do paciente n??o pode ser nulo.");
        } else {
            Paciente verifEmail = this.pacienteRepository.findByEmail(request.getEmail());
            if (!ObjectUtils.isEmpty(verifEmail)) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                        "Opera????o inv??lida! Email j?? cadastrado no sistema.");
            }
        }
        if(StringUtils.isEmpty(request.getSenha().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! A senha do paciente n??o pode ser nula.");
        }
    }

    public List<Paciente> listarTodos() throws DefaultExceptionHandler {
        List<Paciente> pacienteList = pacienteRepository.findAll();
        if (!pacienteList.isEmpty() || pacienteList != null) {
            try {
                return pacienteList;
            } catch (Exception e) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Opera????o inv??lida! Erro leitura paciente.");
            }
        } else {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Opera????o inv??lida! Lista vazia.");
        }
    }

    public Paciente listarPorId(final Integer id) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(id)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Opera????o inv??lida! O campo 'ID' ?? obrigat??rio.");
        }
        try {
            // tendo ID informado, faz a pesquisa no reposit??rio
            // SE tiver dados, RETURN
            // SEN??O cai na DefaultExceptionHandler (tern??rio)
            return this.pacienteRepository.findById(id).orElseThrow(
                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informa????o encontrada para o ID informado.")
            );
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Opera????o inv??lida! Erro leitura paciente.");
        }
    }

    public PageDTO<PacienteDTO> consultarPaginado(int page, int size, PacienteDTO request) throws DefaultExceptionHandler {
        try {
            PageRequest pageable = PageRequest.of(
                    page,
                    size,
                    Sort.Direction.ASC, "nome");

            Page<Paciente> pages;
            if (!org.apache.commons.lang3.ObjectUtils.isEmpty(request)) {
                pages = this.pacienteRepository.findAll(Example.of(super.convertToModel(request, Paciente.class)), pageable);
            } else {
                pages = this.pacienteRepository.findAll(pageable);
            }
            if (!pages.isEmpty()) {
                final long totalElements = pages.getTotalElements();
                final int totalPages = pages.getTotalPages();
                final boolean isFirst = pages.isFirst();
                final boolean isLast = pages.isLast();

                List<PacienteDTO> out = pages.stream()
                        .map(entity -> super.convertToDTO(entity, PacienteDTO.class)).collect(Collectors.toList());

                PageDTO<PacienteDTO> pageDTO = new PageDTO<PacienteDTO>();
                pageDTO.setTotalElements(BigDecimal.valueOf(totalElements));
                pageDTO.setTotalPages(BigDecimal.valueOf(totalPages));
                pageDTO.setFirst(isFirst);
                pageDTO.setLast(isLast);
                pageDTO.setContent(out);

                return pageDTO;
            }
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
        return null;
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public Paciente atualizar(PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        this.validaAtualizarPaciente(pacienteDTO);
        if (ObjectUtils.isEmpty(pacienteDTO) || ObjectUtils.isEmpty(pacienteDTO.getId())) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Opera????o inv??lida! O campo ID ?? obrigat??rio.");
        }
        try {
            Paciente paciente = this.consultarPorId(pacienteDTO.getId());

            paciente.setNome(pacienteDTO.getNome());
//            paciente.setCartaoSus(pacienteDTO.getCartaoSus().replaceAll("\\D", ""));
            paciente.setTelefone(pacienteDTO.getTelefone().replaceAll("\\D", ""));
            paciente.setDataNascimento(pacienteDTO.getDataNascimento());
//            paciente.setDataNascimento(DateTimeUtils.formataData(pacienteDTO.getDataNascimentoFormatada(),"yyyy-MM-dd"));
            paciente.setGenero(pacienteDTO.getGenero().substring(0,1).toUpperCase());
            paciente.setUsername(pacienteDTO.getUsername());
            paciente.setEmail(pacienteDTO.getEmail());

            return pacienteRepository.save(paciente);
        } catch (DefaultExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }

    public Paciente consultarPorId(final Integer id) throws DefaultExceptionHandler{
        if (ObjectUtils.isEmpty(id)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O campo 'id' ?? obrigat??rio.");
        }
        try {
            // tendo ID informado, faz a pesquisa no reposit??rio
            // SE tiver dados, RETURN
            // SEN??O cai na DefaultExceptionHandler (tern??rio)
            return this.pacienteRepository.findById(id).orElseThrow(
                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(),
                            "Nenhuma informa????o encontrada para o ID informado.")
            );
        } catch (DefaultExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }

    private void validaAtualizarPaciente(PacienteDTO request) throws DefaultExceptionHandler{
        if(StringUtils.isEmpty(request.getCartaoSus().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O cart??o SUS do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getTelefone().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O telefone do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getDataNascimento())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! A data de nascimento do paciente n??o pode ser nula.");
        }
        if(StringUtils.isEmpty(request.getGenero().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O genero do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getUsername().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O username do paciente n??o pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getEmail().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Opera????o inv??lida! O email do paciente n??o pode ser nulo.");
        }
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public void ativarPorId(final Integer id) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(id)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Opera????o inv??lida! O campo ID ?? obrigat??rio.");
        }
        try {
            // tendo ID informado, faz a pesquisa no reposit??rio
            // SE tiver dados, Paciente paciente recebe...
            // SEN??O cai na DefaultExceptionHandler (tern??rio)
            Paciente paciente = this.pacienteRepository.findById(id).orElseThrow(
                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informa????o encontrada para os par??metros informados.")
            );
            paciente.setAtivoInativo(EnumAtivoInativo.ATIVO.getCodigo());
            this.pacienteRepository.save(paciente);
        } catch (DefaultExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public void deletarPorId(final Integer id) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(id)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Opera????o inv??lida! O campo ID ?? obrigat??rio.");
        }
        try {
            // tendo ID informado, faz a pesquisa no reposit??rio
            // SE tiver dados, Paciente paciente recebe...
            // SEN??O cai na DefaultExceptionHandler (tern??rio)
            Paciente paciente = this.pacienteRepository.findById(id).orElseThrow(
                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informa????o encontrada para os par??metros informados.")
            );
            // inativar - NAO DELETA DO BANCO, APENAS INATIVA PACIENTE
            paciente.setAtivoInativo(EnumAtivoInativo.INATIVO.getCodigo());
            this.pacienteRepository.save(paciente);
            // deletar - DELETA DO BANCO - comentar linha acima e descomentar linha abaixo
//            this.pacienteRepository.delete(paciente);
        } catch (DefaultExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }
}
