package com.global.ecommerce.repository;

import com.global.ecommerce.Entity.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<RoleModel,Long> {
    RoleModel findByName (String name);
}
