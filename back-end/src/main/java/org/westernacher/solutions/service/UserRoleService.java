package org.westernacher.solutions.service;

import org.westernacher.solutions.domain.entities.UserRole;

public interface UserRoleService
{
    UserRole findRole(String authority);
}
