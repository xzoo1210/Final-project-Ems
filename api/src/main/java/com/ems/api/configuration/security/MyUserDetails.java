package com.ems.api.configuration.security;

import com.ems.api.entity.Account;
import com.ems.api.util.Constant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private String email;
    private String password;
    private Constant.TypeConstant.AccountStatus status;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(Account account) {
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.authorities = Arrays.stream(account.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.status = account.getStatus();
    }

    public MyUserDetails() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
//        grantedAuthorities.add(new SimpleGrantedAuthority(Constant.Role.USER.name()));
//        return Arrays.asList();
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Constant.TypeConstant.AccountStatus.ACTIVE.equals(this.status);
    }
}
