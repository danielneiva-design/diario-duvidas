package br.com.daniel.diario.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Duvida {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final String mensagem;
    private final LocalDateTime datahora;

    public Duvida(String mensagem) {
        this.mensagem = mensagem;
        this.datahora = LocalDateTime.now();
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDatahora() {
        return datahora;
    }

    @Override
    public String toString() {
        return datahora.format(FORMATO) + " - " + mensagem;
    }
}
