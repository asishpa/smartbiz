package com.smartbiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartbiz.entity.Role;
import com.smartbiz.entity.Role.RoleName;
import com.smartbiz.repository.RoleRepository;

@Component
public class RoleInitializer implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepo;
	@Override
	public void run(String... args) throws Exception {
		initializeRoles();
	}

	private void initializeRoles() {
		createRoleIfNotFound(RoleName.BUYER);
        createRoleIfNotFound(RoleName.STORE_OWNER);
        createRoleIfNotFound(RoleName.MANAGER);
        createRoleIfNotFound(RoleName.STAFF);
	}

	private void createRoleIfNotFound(RoleName roleName) {
		if(roleRepo.findByRoleName(roleName).isEmpty()) {
			Role role = new Role();
			role.setRoleName(roleName);
			roleRepo.save(role);
		}
	}
	
}
