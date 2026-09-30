package br.com.daniel.diario;

import br.com.daniel.diario.modelo.Duvida;
import br.com.daniel.diario.repositorio.DiarioArquivo;
import br.com.daniel.diario.repositorio.DuvidaApi;

import java.io.IOException;

public class App {

    public void main() {
        DiarioArquivo diario = new DiarioArquivo("diario.txt");
        DuvidaApi api = new DuvidaApi("http://localhost:8080/duvidas");

        IO.println("\nBem-vindo ao diário de dúvidas!\nAqui você pode registrar suas dúvidas e salvá-las em um arquivo de texto.\n");

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

            try {
                IO.println("Enviado para a API: " + api.enviar(duvida) + "\n");
            } catch (IOException | InterruptedException e) {
                IO.println("Não foi possível enviar para a API (ela está rodando?): " + e.getMessage() + "\n");
            }

            resposta = IO.readln("Deseja registrar outra dúvida?\n1[sim] 0[não]\n").trim();
        } while (resposta.equals("1"));

        IO.println("\nObrigado por usar o diário de dúvidas!");
    }
}
