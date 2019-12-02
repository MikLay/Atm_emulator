package com.atm.model.dto.account.service;


import com.atm.model.dto.account.BankAccount;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder(toBuilder = true)
@Table(name = "surplier")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class Surplie {

    @Id
    @Column(name = "surplier_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer surplierId;


    @Column(name = "surplier_limit")
    private Long surplieLimit;

    @OneToOne(mappedBy = "supplier", fetch = FetchType.LAZY)
    private BankAccount from;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surplier_account_to")
    private BankAccount to;

}
