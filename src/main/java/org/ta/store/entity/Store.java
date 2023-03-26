package org.ta.store.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "TA_STORE")
public class Store implements Serializable {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long sid;

    @Column(name="country_code")
    private String countryCode;

    private String sku;

    @Column(name="prod_name")
    private String prodName;

    private Long price;

    @Column(name="created_by")
    private Long createdBy;

    @Column(name="created_date")
    private LocalDateTime createdDate;

    @OneToMany(targetEntity = LinkCountryStore.class)
    @JoinColumn(name = "cc",referencedColumnName = "country_code",insertable = false,updatable = false)
    private List<LinkCountryStore> storeList;
}
