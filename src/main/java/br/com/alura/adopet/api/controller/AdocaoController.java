package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.StatusNotificacao;
import br.com.alura.adopet.api.service.AdocaoService;
import br.com.alura.adopet.api.service.EmailService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {
    @Autowired
    private AdocaoService adocaoService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> solicitar(@RequestBody @Valid Adocao adocao) {
        try {
            adocaoService.solicitarAdocao(adocao);
            emailService.dispararEmailNotificação(adocao, StatusNotificacao.SOLICITADO);
            return ResponseEntity.ok().build();
        } catch (ValidationException validationException) {
            return ResponseEntity.badRequest().body(validationException.getMessage());
        }
    }

    @PutMapping("/aprovar")
    @Transactional
    public ResponseEntity<String> aprovar(@RequestBody @Valid Adocao adocao) {
        adocaoService.alterarStatus(adocao, StatusAdocao.APROVADO);
        emailService.dispararEmailNotificação(adocao, StatusNotificacao.APROVADO);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/reprovar")
    @Transactional
    public ResponseEntity<String> reprovar(@RequestBody @Valid Adocao adocao) {
        adocaoService.alterarStatus(adocao, StatusAdocao.REPROVADO);
        emailService.dispararEmailNotificação(adocao, StatusNotificacao.REPROVADO);

        return ResponseEntity.ok().build();
    }

}
