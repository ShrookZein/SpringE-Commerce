package com.global.ecommerce.service.authService;

import com.global.ecommerce.Entity.RoleModel;
import com.global.ecommerce.repository.RoleRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2; // Import the annotation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Log4j2 // Add the annotation for Log4j2
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public List<RoleModel> findAll() {
        log.info("Fetching all roles.");
        List<RoleModel> roles = roleRepo.findAll();
        log.debug("Roles fetched: {}", roles);
        return roles;
    }

    public RoleModel findById(Long id) {
        log.info("Finding role by ID: {}", id);
        RoleModel role = roleRepo.findById(id).orElse(null);
        if (role == null) {
            log.warn("Role not found for ID: {}", id);
        } else {
            log.debug("Role found: {}", role);
        }
        return role;
    }

    public RoleModel findByName(String name) {
        log.info("Finding role by name: {}", name);
        RoleModel role = roleRepo.findByName(name);
        if (role == null) {
            log.warn("Role not found for name: {}", name);
        } else {
            log.debug("Role found: {}", role);
        }
        return role;
    }

    public RoleModel save(RoleModel entity) {
        log.info("Saving role: {}", entity.getName());
        RoleModel savedRole = roleRepo.save(entity);
        log.debug("Role saved successfully with ID: {}", savedRole.getId());
        return savedRole;
    }
}






//package com.global.ecommerce.service.authService;
//
//import com.global.ecommerce.Entity.RoleModel;
//import com.global.ecommerce.repository.RoleRepo;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//@NoArgsConstructor
//public class RoleService {
//    @Autowired
//    private RoleRepo roleRepo;
//    public List<RoleModel> findAll(){
//        return roleRepo.findAll();
//    }
//    public RoleModel findById(Long id){
//        return roleRepo.findById(id).orElse(null);
//    }
//
//    public RoleModel findByName(String name){
//        return roleRepo.findByName(name);
//    }
//    public RoleModel save(RoleModel entity){
//        return roleRepo.save(entity);
//    }
//}




