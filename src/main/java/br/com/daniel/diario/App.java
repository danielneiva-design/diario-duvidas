package br.com.daniel.diario;

import br.com.daniel.diario.modelo.Duvida;
import br.com.daniel.diario.repositorio.DiarioArquivo;
import br.com.daniel.diario.repositorio.DuvidaApi;
import br.com.daniel.diario.servico.Sincronizador;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class App {

    public void main() {
        DiarioArquivo diario = new DiarioArquivo("diario.txt");
         // Lê o endereço da API e a chave do arquivo .env (que não vai pro GitHub)
        Properties config = new Properties();
        try (FileReader arquivo = new FileReader(".env")) {
            config.load(arquivo);
        } catch (IOException e) {
            IO.println("Não encontrei o arquivo .env com API_URL e API_KEY. Crie ele na pasta do projeto.");
            return;
        }

        DuvidaApi api = new DuvidaApi(config.getProperty("API_URL"), config.getProperty("API_KEY"));
        Sincronizador sincronizador = new Sincronizador(diario, api);

        IO.println("\nBem-vindo ao diário de dúvidas!\nAqui você pode registrar suas dúvidas e salvá-las em um arquivo de texto.\n");

        // Ao abrir: envia o que ficou para trás (dúvidas antigas ou de quando a API estava fora)
        sincronizar(sincronizador);

        String resposta;
        do {
            String mensagem = IO.readln("Qual é a sua dúvida?\n");
            Duvida duvida = new Duvida(mensagem);

            try {
                diario.salvar(duvida);
                IO.println("Registrado: " + duvida + "\n");
            } catch (IOException e) {
                IO.println("Erro ao salvar no diário: " + e.getMessage() + "\n");
            }

            sincronizar(sincronizador);

            resposta = IO.readln("Deseja registrar outra dúvida?\n1[sim] 0[não]\n").trim();
        } while (resposta.equals("1"));

        IO.println("\nObrigado por usar o diário de dúvidas!");
    }

    private void sincronizar(Sincronizador sincronizador) {
        try {
            int enviadas = sincronizador.sincronizar();
            if (enviadas > 0) {
                IO.println("Sincronizado com a API: " + enviadas + " dúvida(s) enviada(s).\n");
            }
        } catch (IOException | InterruptedException e) {
            IO.println("API indisponível (" + e.getMessage() + "). As dúvidas ficam no diário e serão enviadas na próxima sincronização.\n");
        }
    }
}
