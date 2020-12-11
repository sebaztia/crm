package com.steelerose.crm.service.impl;

import com.steelerose.crm.model.Client;
import com.steelerose.crm.repository.ClientRepository;
import com.steelerose.crm.service.ClientService;
import com.steelerose.crm.web.dto.CallListDto;
import com.steelerose.crm.web.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Lists;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {

        return clientRepository.findAll();
    }

    @Override
    public void saveClient(ClientDto dto) {
        Client client = new Client();
        if (dto.getId() > 0) {
            client.setId(dto.getId());
        }
        client.setFullName(dto.getFullName());
        client.setCallSheet(dto.getCallSheet());
        client.setEmail(dto.getEmail());
        client.setRecordDate(Date.from(dto.getRecordDate().atZone(ZoneId.systemDefault()).toInstant()));
        client.setRefNumber(dto.getRefNumber());
        client.setCallId(dto.getCallId());

        this.clientRepository.save(client);
    }



    @Override
    public ClientDto getClientById(Long id) {
        Optional< Client > optional = clientRepository.findById(id);
        Client client;
        if (optional.isPresent()) {
            client = optional.get();
        } else {
            throw new RuntimeException(" Client not found for id :: " + id);
        }
        return toDto(client);
    }

    private ClientDto toDto(Client client) {
        ClientDto dto = new ClientDto();

        dto.setId(client.getId());
        dto.setFullName(client.getFullName());
        dto.setRecordDate(client.getRecordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        dto.setRefNumber(client.getRefNumber());
        dto.setEmail(client.getEmail());
        dto.setCallSheet(client.getCallSheet());
        dto.setCallId(client.getCallId());

        return dto;
    }

    @Override
    public void deleteClientById(Long id) {
        this.clientRepository.deleteById(id);
    }

    @Override
    public Page<Client> findPaginatedClients(int pageNo, int pageSize, String sortField, String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.clientRepository.findAll(pageable);
    }

    @Override
    public ClientDto getClientByCallListId(Long callId) {
        Client client = clientRepository.findByCallId(callId);
        if (client == null) {
            return null;
        }

        return toDto(client);
    }

    @Override
    public List<ClientDto> getAllByRefNumber(String refNumber, long id) {

        return toModel(clientRepository.findAllByRefNumberAndIdIsNot(refNumber, id));
    }

    private List<ClientDto> toModel(List< Client > clientList) {
        if (clientList == null) {
            return null;
        }
        return clientList.stream().map(this::toDto).collect(Collectors.toList());
    }

}
