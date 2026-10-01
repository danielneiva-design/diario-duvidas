package br.com.daniel.diario.servico;

import br.com.daniel.diario.modelo.Duvida;
import br.com.daniel.diario.modelo.DuvidaSalva;
import br.com.daniel.diario.repositorio.DiarioArquivo;
import br.com.daniel.diario.repositorio.DuvidaApi;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Sincronizador {

    // A API e o .txt podem registrar a mesma dúvida com alguns segundos de diferença
    private static final Duration TOLERANCIA = Duration.ofSeconds(2);

    private final DiarioArquivo diario;
    private final DuvidaApi api;

    public Sincronizador(DiarioArquivo diario, DuvidaApi api) {
        this.diario = diario;
        this.api = api;
    }

    // Envia para a API toda dúvida do .txt que ela ainda não tem. Devolve quantas enviou.
    public int sincronizar() throws IOException, InterruptedException {
        List<Duvida> locais = diario.listar();
        List<DuvidaSalva> naApi = new ArrayList<>(api.listar());

        int enviadas = 0;
        for (Duvida duvida : locais) {
            DuvidaSalva igual = procurar(duvida, naApi);
            if (igual != null) {
                naApi.remove(igual); // já está lá: não conta de novo para outra linha igual
            } else {
                api.enviar(duvida);
                enviadas++;
            }
        }
        return enviadas;
    }

    private DuvidaSalva procurar(Duvida duvida, List<DuvidaSalva> naApi) {
        for (DuvidaSalva salva : naApi) {
            boolean mesmaMensagem = salva.mensagem().equals(duvida.getMensagem());
            Duration diferenca = Duration.between(salva.datahora(), duvida.getDatahora()).abs();
            if (mesmaMensagem && diferenca.compareTo(TOLERANCIA) <= 0) {
                return salva;
            }
        }
        return null;
    }
}
