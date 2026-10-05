package br.com.daniel.diario.repositorio;

import tools.jackson.databind.json.JsonMapper;
import br.com.daniel.diario.modelo.Duvida;
import br.com.daniel.diario.modelo.DuvidaSalva;
import tools.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class DuvidaApi {

    private final String url;
    private final String chave;

    private final HttpClient cliente = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();
    private final JsonMapper json = JsonMapper.builder().build();

    public DuvidaApi(String url, String chave) {
        this.url = url;
        this.chave = chave;
    }

    // Faz o mesmo que o Insomnia: POST /duvidas com {"mensagem": "...", "datahora":
    // "..."} no corpo
    public String enviar(Duvida duvida) throws IOException, InterruptedException {
        String corpo = json.writeValueAsString(duvida);
        HttpRequest pedido = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .header("X-API-Key", chave)
                .POST(HttpRequest.BodyPublishers.ofString(corpo))
                .build();

        HttpResponse<String> resposta = cliente.send(pedido, HttpResponse.BodyHandlers.ofString());

        if (!deuCerto(resposta)) {
            throw new IOException("a API respondeu " + resposta.statusCode());
        }
        return resposta.body();
    }

    // GET /duvidas: pergunta pra API quais dúvidas ela já tem
    public List<DuvidaSalva> listar() throws IOException, InterruptedException {
        HttpRequest pedido = HttpRequest.newBuilder(URI.create(url))
                .header("X-API-Key", chave)
                .GET()
                .build();

        HttpResponse<String> resposta = cliente.send(pedido, HttpResponse.BodyHandlers.ofString());

        if (!deuCerto(resposta)) {
            throw new IOException("a API respondeu " + resposta.statusCode());
        }
        return json.readValue(resposta.body(), new TypeReference<List<DuvidaSalva>>() {
        });
    }

    // Qualquer código 2xx é sucesso: 200 (OK), 201 (Created), 204 (No Content)...
    private boolean deuCerto(HttpResponse<String> resposta) {
        return resposta.statusCode() >= 200 && resposta.statusCode() < 300;
    }
}
