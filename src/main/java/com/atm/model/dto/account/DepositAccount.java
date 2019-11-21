package com.atm.model.dto.account;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@Entity
@SuperBuilder
@Table(name = "deposit_bank_account")
@DiscriminatorValue("DEPOSIT")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class DepositAccount extends BankAccount {

    @Column(name = "deposit_bank_account_percent")
    private String depositAccountPercent;

    @Override
    public String toString() {
        return "DepositAccount{" +
                "depositAccountPercent='" + depositAccountPercent + '\'' +
                "} " + super.toString();
    }
}
