package com.ems.api.service.impl;

import com.ems.api.entity.Account;
import com.ems.api.repository.AccountRepo;
import com.ems.api.configuration.security.MyUserDetails;
import com.ems.api.util.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = AppException.class)
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> account = accountRepo.findAccountByEmail(email);
        account.orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));

        return account.map(MyUserDetails::new).get();
    }
}
