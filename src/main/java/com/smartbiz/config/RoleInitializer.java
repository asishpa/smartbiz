package com.smartbiz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartbiz.entity.Role;
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
		createRoleIfNotFound("BUYER");
        createRoleIfNotFound("STORE_OWNER");
	}

	private void createRoleIfNotFound(String roleName) {
		if(roleRepo.findByRoleName(roleName).isEmpty()) {
			Role role = new Role();
			role.setRoleName(roleName);
			roleRepo.save(role);
		}
	}
	
}
