package org.ta.store.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name="TA_CC_STORE")
@IdClass(LinkCountryStoreID.class)
public class LinkCountryStore implements Serializable {

    @Id
    private String cc;

    @Id
    private Long sid;
}
