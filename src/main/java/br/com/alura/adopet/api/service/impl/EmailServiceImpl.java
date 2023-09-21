package br.com.alura.adopet.api.service.impl;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusNotificacao;
import br.com.alura.adopet.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void dispararEmailNotificação(Adocao adocao, StatusNotificacao statusNotificacao) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("adopet@email.com.br");
        email.setFrom(adocao.getTutor().getEmail());
        email.setSubject("Adoção " + statusNotificacao.toString());
        email.setText(statusNotificacao.statusRetorno() + "Nome do pet:" + adocao.getPet().getNome() + " Data solicitação: " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        emailSender.send(email);
    }
}
