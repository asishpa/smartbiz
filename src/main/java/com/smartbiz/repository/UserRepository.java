package com.smartbiz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
	@Query("SELECT r.roleName FROM User u JOIN u.roles r WHERE u.email = :email")
	List<String> findRoleNamesByEmail(@Param("email") String email);


}
