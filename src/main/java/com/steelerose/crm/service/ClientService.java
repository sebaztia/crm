package com.steelerose.crm.service;

import com.steelerose.crm.model.Client;
import com.steelerose.crm.web.dto.CallListDto;
import com.steelerose.crm.web.dto.ClientDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientService {

    List<Client> getAllClients();

    void saveClient(ClientDto dto);

    ClientDto getClientById(Long id);

    void deleteClientById(Long id);

    Page<Client> findPaginatedClients(int pageNo, int pageSize, String sortField, String sortDirection);

    List<ClientDto> getAllByRefNumber(String refNumber, long id);

    ClientDto getClientByCallListId(Long callId);
}
