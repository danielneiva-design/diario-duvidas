package br.com.daniel.diario.repositorio;

import br.com.daniel.diario.modelo.Duvida;

import java.io.FileWriter;
import java.io.IOException;

public class DiarioArquivo {

    private final String caminho;

    public DiarioArquivo(String caminho) {
        this.caminho = caminho;
    }

    public void salvar(Duvida duvida) throws IOException {
        try (FileWriter arquivo = new FileWriter(caminho, true)) {
            arquivo.write(duvida + "\n");
        }
    }
}
