package br.com.alura.adopet.api.service.impl;

import br.com.alura.adopet.api.dto.AdocaoDto;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.StatusNotificacao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.service.AdocaoService;
import br.com.alura.adopet.api.service.EmailService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdocaoServiceImpl implements AdocaoService {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private EmailService emailService;
    @Override
    public void alterarStatus(AdocaoDto adocaoDto, StatusAdocao statusAdocao) {
        Adocao adocao = adocaoRepository.findById(adocaoDto.idAdocao()).get();
        adocao.setStatus(statusAdocao);
        adocaoRepository.save(adocao);
        if(statusAdocao == StatusAdocao.APROVADO) {
            emailService.dispararEmailNotificação(adocao, StatusNotificacao.APROVADO);
        } else{
            emailService.dispararEmailNotificação(adocao, StatusNotificacao.REPROVADO);
        }
    }

    @Override
    public void solicitarAdocao(AdocaoDto dto) {
        Adocao adocao = adocaoRepository.findById(dto.idAdocao()).get();
        if (adocao.getPet().getAdotado() == true) {
            throw new ValidationException("Pet já foi adotado!");
        } else {
            List<Adocao> adocoes = adocaoRepository.findAll();
            for (Adocao a : adocoes) {
                if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidationException("Tutor já possui outra adoção aguardando avaliação!");
                }
            }
            for (Adocao a : adocoes) {
                if (a.getPet() == adocao.getPet() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidationException("Pet já está aguardando avaliação para ser adotado!");
                }
            }
            for (Adocao a : adocoes) {
                int contador = 0;
                if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.APROVADO) {
                    contador = contador + 1;
                }
                if (contador == 5) {
                    throw new ValidationException("Tutor chegou ao limite máximo de 5 adoções!");
                }
            }
        }
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocaoRepository.save(adocao);
        emailService.dispararEmailNotificação(adocao, StatusNotificacao.SOLICITADO);
    }
}
