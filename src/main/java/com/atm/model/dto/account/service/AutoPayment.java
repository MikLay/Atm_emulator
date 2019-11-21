package com.atm.model.dto.account.service;


import com.atm.model.dto.account.BankAccount;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Builder(toBuilder = true)
@Table(name = "auto_payment")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class AutoPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auto_payment_id")
    private Integer autoPaymentId;

    @Column(name = "auto_payment_amount")
    private Long autoPaymentAmount;

    @Column(name = "auto_payment_start")
    private Date autoPaymentStart;

    @Column(name = "auto_payment_end")
    private Date autoPaymentEnd;

    @Column(name = "auto_payment_frequency")
    private Integer autoPaymentFrequency;

    @Column(name = "auto_payment_fixed")
    private Boolean autoPaymentIsFixed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_payment_bank_account_id_from")
    private BankAccount paymentFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_payment_bank_account_id_to")
    private BankAccount paymentTo;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AutoPayment)) return false;
        AutoPayment that = (AutoPayment) o;
        return Objects.equals(getAutoPaymentId(), that.getAutoPaymentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAutoPaymentId());
    }

    @Override
    public String toString() {
        return "AutoPayment{" +
                "autoPaymentId=" + autoPaymentId +
                ", autoPaymentAmount=" + autoPaymentAmount +
                ", autoPaymentStart=" + autoPaymentStart +
                ", autoPaymentEnd=" + autoPaymentEnd +
                ", autoPaymentFrequency=" + autoPaymentFrequency +
                ", autoPaymentIsFixed=" + autoPaymentIsFixed +
                ", paymentFrom=" + paymentFrom +
                ", paymentTo=" + paymentTo +
                '}';
    }
}
