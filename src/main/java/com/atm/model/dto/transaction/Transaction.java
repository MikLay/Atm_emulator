package com.atm.model.dto.transaction;


import com.atm.model.dto.ATM;
import com.atm.model.dto.account.BankAccount;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder(toBuilder = true)
@Table(name = "transaction")
@DiscriminatorColumn(name = "transaction_type")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "transaction_amount")
    private Long transactionAmount;

    @Column(name = "transaction_status")
    private Boolean transactionStatus;

    @Column(name = "transaction_commission")
    private Long transactionCommission;

    @Column(name = "transaction_type")
    private String transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_account_id_from", nullable = true)
    private BankAccount bankAccountFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_account_id_to", nullable = true)
    private BankAccount bankAccountTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transaction_atm_id", nullable = true)
    private ATM atm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(getTransactionId(), that.getTransactionId()) &&
                Objects.equals(getTransactionAmount(), that.getTransactionAmount()) &&
                Objects.equals(getTransactionStatus(), that.getTransactionStatus()) &&
                Objects.equals(getTransactionCommission(), that.getTransactionCommission()) &&
                Objects.equals(getTransactionType(), that.getTransactionType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactionId(), getTransactionAmount(), getTransactionStatus(), getTransactionCommission(), getTransactionType());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionAmount=" + transactionAmount +
                ", transactionStatus=" + transactionStatus +
                ", transactionCommission=" + transactionCommission +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
