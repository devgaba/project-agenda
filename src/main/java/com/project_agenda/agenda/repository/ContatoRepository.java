package com.project_agenda.agenda.repository;


import com.project_agenda.agenda.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, UUID> {
    Optional<Contato> findByEmail (String email);
}
