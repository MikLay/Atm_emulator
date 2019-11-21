package com.atm.model.dto;


import com.atm.model.dto.account.BankAccount;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Builder(toBuilder = true)
@Table(name = "client")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer clientId;

    @Column(name="client_firstname")
    private String clientFirstName;

    @Column(name="client_lastname")
    private String clientLastName;

    @Column(name="client_birthdate")
    private Date clientBirthDate;

    @Column(name="client_phone_number")
    private String clientPhoneNumber;

    @Column(name="client_email")
    private String clientEmail;

    @OneToMany(mappedBy = "account_client", cascade = CascadeType.ALL)
    private List<BankAccount> accounts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(getClientId(), client.getClientId()) &&
                Objects.equals(getClientBirthDate(), client.getClientBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientId(), getClientBirthDate());
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", clientFirstName='" + clientFirstName + '\'' +
                ", clientLastName='" + clientLastName + '\'' +
                ", clientBirthDate=" + clientBirthDate +
                ", clientPhoneNumber='" + clientPhoneNumber + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                '}';
    }
}
