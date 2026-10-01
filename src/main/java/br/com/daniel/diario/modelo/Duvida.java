package br.com.daniel.diario.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Duvida {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final String mensagem;
    private final LocalDateTime datahora;

    public Duvida(String mensagem) {
        this(mensagem, LocalDateTime.now());
    }

    public Duvida(String mensagem, LocalDateTime datahora) {
        this.mensagem = mensagem;
        this.datahora = datahora;
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

    public static Duvida deLinha(String linha) {
        String[] partes = linha.split(" - ", 2);
        LocalDateTime datahora = LocalDateTime.parse(partes[0], FORMATO);
        String mensagem = partes[1];
        return new Duvida(mensagem, datahora);
    }
}
