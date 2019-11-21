package com.atm.model.dao;

import com.atm.model.dto.Client;

public interface ClientDao extends Dao<Client, Integer> {
    Client findByAccount(Integer accountNumber);
}
