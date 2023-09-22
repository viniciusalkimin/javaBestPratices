package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;

public interface AdocaoService {
    void alterarStatus(AdocaoDto dto, StatusAdocao statusAdocao);

    void solicitarAdocao(SolicitacaoAdocaoDto dto);
}
