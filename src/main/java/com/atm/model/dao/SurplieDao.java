package com.atm.model.dao;

import com.atm.model.dto.account.service.Surplie;

import java.util.List;

public interface SurplieDao extends Dao<Surplie, Integer> {
    List<Surplie> getBySender(Integer accountNumber);


}
