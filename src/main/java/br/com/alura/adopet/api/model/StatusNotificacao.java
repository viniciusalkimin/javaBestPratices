package br.com.alura.adopet.api.model;

public enum StatusNotificacao {

    SOLICITADO {

        @Override
        public String statusRetorno() {
            return "Parabéns, sua solicitação de adoção foi regitrada, favor entrar em contato com a clínica. /n Dados da solicitação:";
        }
    },
    APROVADO {
        @Override
        public String statusRetorno() {
            return "Parabéns, sua solicitação de adoção foi aprovada, favor entrar em contato com a clínica. /n Dados da solicitação:";
        }
    },
    REPROVADO {
        @Override
        public String statusRetorno() {
            return "Sinto muito, sua solicitação de adoção foi reprovada, favor entrar em contato com a clínica. /n Dados da solicitação:";
        }
    };

    public abstract String statusRetorno();
}
