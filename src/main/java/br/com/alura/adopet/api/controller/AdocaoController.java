package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AdocaoDto;
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
    public ResponseEntity<String> solicitar(@RequestBody @Valid AdocaoDto adocaoDto) {
        try {
            adocaoService.solicitarAdocao(adocaoDto);

            return ResponseEntity.ok().build();
        } catch (ValidationException validationException) {
            return ResponseEntity.badRequest().body(validationException.getMessage());
        }
    }

    @PutMapping("/aprovar")
    @Transactional
    public ResponseEntity<String> aprovar(@RequestBody @Valid AdocaoDto dto) {
        adocaoService.alterarStatus(dto, StatusAdocao.APROVADO);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/reprovar")
    @Transactional
    public ResponseEntity<String> reprovar(@RequestBody @Valid AdocaoDto dto) {
        adocaoService.alterarStatus(dto, StatusAdocao.REPROVADO);

        return ResponseEntity.ok().build();
    }

}
