package com.atm.model.dto.account;

import com.atm.model.dto.Client;
import com.atm.model.dto.account.service.AutoPayment;
import com.atm.model.dto.account.service.Surplie;
import com.atm.model.dto.transaction.Transaction;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "bank_account")
@DiscriminatorColumn(name = "account_type")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class BankAccount {
    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type", nullable = false, length = 250)
    private String accountType;

    @Column(name = "account_start_date", nullable = false)
    private Date accountStartDate;

    @Column(name = "account_end_date", nullable = false)
    private Date accountEndDate;

    @Column(name = "account_salt", nullable = false)
    private String accountSalt;

    @Column(name = "account_password", nullable = false)
    private String accountPassword;


    @Setter(value = AccessLevel.PUBLIC)
    @Column(name = "account_amount", nullable = false)
    private Long accountAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_client_id", nullable = false)
    private Client account_client;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_surplier_id")
    private Surplie supplier;

    @OneToMany(mappedBy = "paymentFrom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AutoPayment> accountAutoPayments;

    @OneToMany(mappedBy = "paymentTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AutoPayment> accountAutoPaymentsTo;


    @OneToMany(mappedBy = "bankAccountFrom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> accountTransactionsFrom;

    @OneToMany(mappedBy = "bankAccountTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> accountTransactionsTo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(getAccountType(), that.getAccountType()) &&
                Objects.equals(getAccountNumber(), that.getAccountNumber()) &&
                Objects.equals(getAccountStartDate(), that.getAccountStartDate()) &&
                Objects.equals(getAccountEndDate(), that.getAccountEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountType(), getAccountNumber(), getAccountStartDate(), getAccountEndDate());
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                ", accountType='" + accountType + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountStartDate=" + accountStartDate +
                ", accountEndDate=" + accountEndDate +
                ", account_client=" + account_client +
                ", account_autopayments=" + accountAutoPayments +
                ", account_transactionsTo=" + accountTransactionsTo +
                '}';
    }
}
