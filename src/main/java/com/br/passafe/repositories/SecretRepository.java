package com.br.passafe.repositories;

import com.br.passafe.entities.Secret;
import com.br.passafe.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecretRepository extends JpaRepository<Secret, Long> {
    List<Secret> findByOwner(Usuario owner);
}
