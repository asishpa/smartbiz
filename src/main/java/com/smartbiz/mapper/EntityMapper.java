package com.smartbiz.mapper;

import com.smartbiz.dto.UserDTO;
import com.smartbiz.dto.StoreDTO;
import com.smartbiz.dto.RoleDTO;
import com.smartbiz.entity.User;
import com.smartbiz.entity.Store;
import com.smartbiz.entity.Role;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setRoleNames(user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet()));
        if (user.getStore() != null) {
            dto.setStoreName(user.getStore().getName());
        }
        return dto;
    }

    public StoreDTO toStoreDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setCreatedAt(store.getCreatedAt());
        if (store.getOwner() != null) {
            dto.setOwnerName(store.getOwner().getUserName());
        }
        return dto;
    }

    public RoleDTO toRoleDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        return dto;
    }
}