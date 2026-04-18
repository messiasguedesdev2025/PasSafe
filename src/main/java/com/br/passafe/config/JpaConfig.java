package com.br.passafe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//serve para uma única e importante missão: Ativar as Datas Automáticas no seu banco de dados.

@Configuration
@EnableJpaAuditing
public class JpaConfig {
}