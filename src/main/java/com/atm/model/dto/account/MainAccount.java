package com.atm.model.dto.account;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@SuperBuilder
@Table(name = "main_bank_account")
@DiscriminatorValue("MAIN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
public class MainAccount extends BankAccount {

    @Override
    public String toString() {
        return "MainAccount{} " + super.toString();
    }
}
