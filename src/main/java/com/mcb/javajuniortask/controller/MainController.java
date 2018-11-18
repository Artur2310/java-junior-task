package com.mcb.javajuniortask.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mcb.javajuniortask.dto.ClientDTO;
import com.mcb.javajuniortask.service.ClientService;

@RestController
public class MainController {

	@Autowired
	private ClientService clientService;

	
	@GetMapping(value = "/show-all-clients") 
	public Iterable<ClientDTO> showAllClients() {
		return clientService.showAllClients();
	}

	
    @GetMapping("/add-client")
    public UUID addClient(@RequestParam(name = "name") String name) {
        return clientService.addClient(name);
    }

    @GetMapping("/add-debt-to-client")
    public UUID addDebtToClient(@RequestParam(name = "clientId") UUID clientId,
                                @RequestParam(name = "value") BigDecimal value) {
        return clientService.addDebtToClient(clientId, value);
    }

	@GetMapping(value = "/add-payment-by-debt")
	public UUID reduceDebtOfClient(@RequestParam(name = "clientId") UUID clientId, @RequestParam(name = "debtId") UUID debtId,
			@RequestParam(name = "value") BigDecimal value) {
		
		return clientService.addPaymentByDebt(clientId, debtId, value);
	}
}
