package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;

public interface AdocaoService {
    void alterarStatus(Adocao adocao, StatusAdocao statusAdocao);

    void solicitarAdocao(Adocao adocao);
}
