package com.atm.model.dto.account;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@SuperBuilder
@Table(name = "credit_bank_account")
@DiscriminatorValue("CREDIT")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class CreditAccount extends BankAccount {
    @Column(name="credit_bank_account_limit")
    private Long accountCreditLimit;

    @Override
    public String toString() {
        return "CreditAccount{" +
                "accountCreditLimit=" + accountCreditLimit +
                "} " + super.toString();
    }
}
