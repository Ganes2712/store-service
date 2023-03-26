package org.ta.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ta.store.entity.LinkCountryStore;
import org.ta.store.entity.LinkCountryStoreID;

import java.util.Set;

@Repository
public interface LinkCountryStoreRepo extends JpaRepository<LinkCountryStore, LinkCountryStoreID>
{

    public Set<LinkCountryStore> findByCc(String countryCode);

}
