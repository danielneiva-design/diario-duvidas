package br.com.daniel.diario.modelo;

import java.time.LocalDateTime;

// Dúvida como a API devolve: já salva no banco, com protocolo (id)
public record DuvidaSalva(Long id, String mensagem, LocalDateTime datahora) {
}
