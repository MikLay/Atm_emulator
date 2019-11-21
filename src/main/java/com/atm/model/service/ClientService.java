package com.atm.model.service;

import com.atm.model.dto.Client;

public interface ClientService {
    Client getById(Integer  clientId);

    Client getByBankAccount(Integer accountId);
}
