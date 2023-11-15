package org.westernacher.solutions;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.westernacher.solutions.domain.entities.Log;
import org.westernacher.solutions.domain.entities.Operation;
import org.westernacher.solutions.domain.entities.UserRole;
import org.westernacher.solutions.service.UserRoleService;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRoleServiceTestsSpringContext
{
    @Autowired
    private UserRoleService userRoleServiceService;


    @Test
    public void testFindRole_withAuthorityRootAdmin() {

        String authority = "ROOT-ADMIN";
        UserRole userRole = this.userRoleServiceService.findRole(authority);

        assertEquals("Authority is not correct.", authority, userRole.getAuthority());
    }

    @Test
    public void testFindRole_withAuthorityAdmin() {

        String authority = "ADMIN";
        UserRole userRole = this.userRoleServiceService.findRole(authority);

        assertEquals("Authority is not correct.", authority, userRole.getAuthority());
    }

    @Test
    public void testFindRole_withAuthorityModerator() {

        String authority = "MODERATOR";
        UserRole userRole = this.userRoleServiceService.findRole(authority);

        assertEquals("Authority is not correct.", authority, userRole.getAuthority());
    }

    @Test
    public void testFindRole_withAuthorityRoleUser() {

        String authority = "ROLE_USER";
        UserRole userRole = this.userRoleServiceService.findRole(authority);

        assertEquals("Authority is not correct.", authority, userRole.getAuthority());
    }
}
