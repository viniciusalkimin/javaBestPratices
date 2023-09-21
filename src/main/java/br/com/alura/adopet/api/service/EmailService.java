package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusNotificacao;

public interface EmailService {

    void dispararEmailNotificação(Adocao adocao, StatusNotificacao statusNotificacao);
}
