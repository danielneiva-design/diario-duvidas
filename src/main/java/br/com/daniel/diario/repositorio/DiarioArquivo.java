package br.com.daniel.diario.repositorio;

import br.com.daniel.diario.modelo.Duvida;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class DiarioArquivo {

    private final String caminho;
    
    public DiarioArquivo(String caminho) {
        this.caminho = caminho;
    }

    public List<Duvida> listar() throws IOException {
        Path arquivo = Path.of(caminho);
        List<Duvida> duvidas = new ArrayList<>();
            if (!Files.exists(arquivo)) {
            return duvidas;
        }
        for (String linha : Files.readAllLines(arquivo)) {
            if (linha.isBlank()) {
                continue;
            }
                Duvida duvida = Duvida.deLinha(linha);
                duvidas.add(duvida);
            }return duvidas;
        }
    

   

    public void salvar(Duvida duvida) throws IOException {
        try (FileWriter arquivo = new FileWriter(caminho, true)) {
            arquivo.write(duvida + "\n");
        }
    }
}
