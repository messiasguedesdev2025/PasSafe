package com.br.passafe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "PasSafe API", version = "1.0", description = "API do Cofre de Senhas PasSafe - Criptografia AES-256 e JWT"))

public class PassafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassafeApplication.class, args);
	}

}
