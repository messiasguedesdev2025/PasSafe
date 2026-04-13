package com.br.passafe.repositories;

import com.br.passafe.entities.PasswordHistory;
import com.br.passafe.entities.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findBySecretOrderByCreatedAtDesc(Secret secret);
}
