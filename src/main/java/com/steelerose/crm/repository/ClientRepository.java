package com.steelerose.crm.repository;

import com.steelerose.crm.model.Client;
import com.steelerose.crm.web.dto.ClientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByCallId(Long callId);
    List<Client> findAllByRefNumberAndIdIsNot(String refNumber, Long id);
}
