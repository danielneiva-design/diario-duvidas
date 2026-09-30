package br.com.daniel.diario.repositorio;

import br.com.daniel.diario.modelo.Duvida;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class DuvidaApi {

    private final String url;
    private final HttpClient cliente = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    public DuvidaApi(String url) {
        this.url = url;
    }

    // Faz o mesmo que o Insomnia: POST /duvidas com {"mensagem": "..."} no corpo
    public String enviar(Duvida duvida) throws IOException, InterruptedException {
        String json = "{\"mensagem\": \"" + escapar(duvida.getMensagem()) + "\"}";

        HttpRequest pedido = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resposta = cliente.send(pedido, HttpResponse.BodyHandlers.ofString());

        if (resposta.statusCode() != 200) {
            throw new IOException("a API respondeu " + resposta.statusCode());
        }
        return resposta.body();
    }

    // Aspas e barras dentro da mensagem quebrariam o JSON
    private String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
