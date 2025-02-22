package com.itexus.dto.converters;

import com.itexus.domain.Role;
import com.itexus.dto.RoleDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleConvertors {

    public RoleDTO toDTO(Role role) {
        return Optional.ofNullable(role).map(r -> RoleDTO.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .build())
                .orElse(null);
    }

    public Role fromDTO(RoleDTO roleDTO) {
        return Optional.ofNullable(roleDTO).map(rd -> Role.builder()
                        .name(rd.getName())
                        .build())
                .orElse(null);
    }
}
