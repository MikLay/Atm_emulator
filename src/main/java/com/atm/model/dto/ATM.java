package com.atm.model.dto;


import com.atm.model.dto.transaction.Transaction;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Builder(toBuilder = true)
@Table(name = "atm")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class ATM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "atm_id")
    private Integer atmId;

    @Column(name = "atm_address")
    private String atmAddress;

    @Column(name = "atm_amount")
    private Long atmAmount;

    @OneToMany(mappedBy = "atm", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ATM)) return false;
        ATM atm = (ATM) o;
        return Objects.equals(getAtmId(), atm.getAtmId()) &&
                Objects.equals(getAtmAddress(), atm.getAtmAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAtmId(), getAtmAddress());
    }

    @Override
    public String toString() {
        return "ATM{" +
                "atmId=" + atmId +
                ", atmAddress='" + atmAddress + '\'' +
                ", atmAmount=" + atmAmount +
                ", transactions=" + transactions +
                '}';
    }
}
