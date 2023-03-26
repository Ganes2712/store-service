package org.ta.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.ta.store.entity.Store;

@Repository
public interface StoreRepo extends JpaRepository<Store,Long> , JpaSpecificationExecutor<Store> {
}
