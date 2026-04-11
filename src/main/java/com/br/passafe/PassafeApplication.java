package com.br.passafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Ativa a criação automática de datas (createdAt, updatedAt)
public class PassafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassafeApplication.class, args);
	}

}
