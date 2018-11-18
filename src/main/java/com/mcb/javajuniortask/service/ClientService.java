package com.mcb.javajuniortask.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;

import com.mcb.javajuniortask.dto.ClientDTO;
import com.mcb.javajuniortask.model.Client;
import com.mcb.javajuniortask.model.Debt;
import com.mcb.javajuniortask.model.Payment;
import com.mcb.javajuniortask.repository.ClientRepository;

@ShellComponent
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @ShellMethod("Shows all clients in db")
    @Transactional
    public Iterable<ClientDTO> showAllClients() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false).map(client -> {
            ClientDTO clientDTO = new ClientDTO();
            clientDTO.setId(client.getId());
            clientDTO.setName(client.getName());
            clientDTO.setTotalDebt(client.getDebts().stream().map(Debt::getValue).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            return clientDTO;
        }).collect(Collectors.toList());
        
    }

    @ShellMethod("Adds client to db")
    @Transactional
    public UUID addClient(@ShellOption String name) {
        Client client = new Client();
        client.setName(name);
        client.setId(UUID.randomUUID());
        client = clientRepository.save(client);
        return client.getId();
    }

    @ShellMethod("Adds debt to client")
    @Transactional
    public UUID addDebtToClient(@ShellOption UUID clientId, @ShellOption BigDecimal value) {
        Client client = clientRepository.findOne(clientId);
        Debt debt = new Debt();
        debt.setValue(value);
        debt.setId(UUID.randomUUID());
        debt.setClient(client);
        client.getDebts().add(debt);
        clientRepository.save(client);
        return debt.getId();
    }
    
    @ShellMethod("Add payment by debt")
    @Transactional
    public UUID addPaymentByDebt(@ShellOption UUID clientId, @ShellOption UUID debtId, @ShellOption BigDecimal value){
    	Client client = Optional.ofNullable(clientRepository.findOne(clientId)).orElseThrow(() -> new RuntimeException("Client with this id not found"));
    	Debt debt = client.getDebts().stream().filter(d -> d.getId().equals(debtId)).findAny().orElseThrow(() -> new RuntimeException("Debt with this id not found"));
    	
    	Optional.ofNullable(value).filter(v -> v.compareTo(BigDecimal.ZERO) > 0 && v.compareTo(debt.getValue()) <= 0)
    	.orElseThrow(()-> new IllegalArgumentException("value for payment is not valid"));
        
    	debt.setValue(debt.getValue().subtract(value));
        
        Payment pay = new Payment();
        pay.setId(UUID.randomUUID());
        pay.setValue(value);
        pay.setClient(client);
        
        client.getPayments().add(pay);
        
        return pay.getId();
    }

}
