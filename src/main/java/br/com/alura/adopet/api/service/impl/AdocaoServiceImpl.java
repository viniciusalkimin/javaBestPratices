package br.com.alura.adopet.api.service.impl;

import br.com.alura.adopet.api.dto.AdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
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
    private TutorRepository tutorRepository;

    @Autowired
    private PetRepository petRepository;

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
    public void solicitarAdocao(SolicitacaoAdocaoDto dto) {
        Tutor tutor = tutorRepository.findById(dto.idTutor()).get();
        Pet pet = petRepository.findById(dto.idPet()).get();

        if (pet.getAdotado() == true) {
            throw new ValidationException("Pet já foi adotado!");
        } else {
            List<Adocao> adocoes = adocaoRepository.findAll();
            for (Adocao a : adocoes) {
                if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidationException("Tutor já possui outra adoção aguardando avaliação!");
                }
            }
            for (Adocao a : adocoes) {
                if (a.getPet() == pet && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidationException("Pet já está aguardando avaliação para ser adotado!");
                }
            }
            for (Adocao a : adocoes) {
                int contador = 0;
                if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
                    contador = contador + 1;
                }
                if (contador == 5) {
                    throw new ValidationException("Tutor chegou ao limite máximo de 5 adoções!");
                }
            }
        }
        Adocao adocao = new Adocao(LocalDateTime.now(),tutor, pet, dto.motivo(), StatusAdocao.AGUARDANDO_AVALIACAO );
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocaoRepository.save(adocao);
        emailService.dispararEmailNotificação(adocao, StatusNotificacao.SOLICITADO);
    }
}
