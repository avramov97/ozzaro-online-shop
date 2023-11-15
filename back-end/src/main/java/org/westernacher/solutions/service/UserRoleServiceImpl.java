package org.westernacher.solutions.service;

import org.westernacher.solutions.domain.entities.UserRole;
import org.westernacher.solutions.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl implements UserRoleService
{
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository)
    {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole findRole(String authority)
    {
        UserRole getRole = this.userRoleRepository.findByAuthority(authority).orElse(null);

        if(getRole != null)
        {
            return getRole;
        }
        else
        {
            return this.userRoleRepository.findByAuthority("ROLE_USER").get();
        }
    }
}
