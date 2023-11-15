package org.westernacher.solutions.domain.models.service;

import org.westernacher.solutions.domain.entities.UserRole;

import java.util.Date;
import java.util.Set;

public class UserServiceModel
{
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Date birthDate;
    private String password;
    private String confirmPassword;

    public String getConfirmPassword()
    {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public UserServiceModel() {
    }

    public UserServiceModel(String firstName, String lastName, String username, String email, Date birthDate, String password)
    {
        this.id = "WDkovfrdkghiodjfidsjbefsrd";
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private Set<UserRole> authorities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Set<UserRole> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }

    public String extractAuthority()
    {
        String authority = null;

        if(this.getAuthorities().stream().filter(o -> o.getAuthority().equals("ROOT-ADMIN")).findFirst().isPresent() == true)
        {
            authority = "ROOT-ADMIN";
        }
        else if(this.getAuthorities().stream().filter(o -> o.getAuthority().equals("ADMIN")).findFirst().isPresent() == true)
        {
            authority = "ADMIN";
        }
        else if(this.getAuthorities().stream().filter(o -> o.getAuthority().equals("MODERATOR")).findFirst().isPresent() == true)
        {
            authority = "MODERATOR";
        }
        else if(this.getAuthorities().stream().filter(o -> o.getAuthority().equals("ROLE_USER")).findFirst().isPresent() == true)
        {
            authority = "ROLE_USER";
        }

        return authority;
    }


}
