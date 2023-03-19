package com.ems.api.service.impl;

import com.ems.api.dto.request.ChangePasswordRequest;
import com.ems.api.dto.request.CreateContactRequest;
import com.ems.api.dto.request.LoginRequest;
import com.ems.api.dto.request.RegisterRequest;
import com.ems.api.dto.response.LoginResponse;
import com.ems.api.entity.Account;
import com.ems.api.entity.Contact;
import com.ems.api.repository.AccountRepo;
import com.ems.api.repository.ContactRepo;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = AppException.class)
public class AuthenticationServiceImpl {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsServiceImpl userDetailsService;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${server.web}")
    private String domainWeb;

    public LoginResponse login(LoginRequest request) throws AppException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),
                            request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException be) {
            throw new AppException(be, Constant.ErrorCode.WRONG_USERNAME_OR_PASSWORD.name());
        } catch (DisabledException de) {
            throw new AppException(de, Constant.ErrorCode.ACCOUNT_NOT_ACTIVE.name());
        } catch (LockedException ef) {
            throw new AppException(ef, Constant.ErrorCode.ACCOUNT_LOCKED.name());
        } catch (Exception e) {
            throw new AppException(e, Constant.ErrorCode.SYSTEM_ERROR.name());
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//        final List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());

        return jwtUtil.generateToken(userDetails, request.isRememberMe());
    }

    public Account register(RegisterRequest registerRequest) throws AppException {
        Optional<Account> existedAccount = accountRepo.findById(registerRequest.getEmail());
        if (existedAccount.isPresent()) {
            if (Constant.TypeConstant.AccountStatus.ACTIVE.equals(existedAccount.get().getStatus())) {
                throw new AppException(Constant.ErrorCode.ACCOUNT_EXISTED.name());
            }
            if (Constant.TypeConstant.AccountStatus.NOT_VALIDATE.equals(existedAccount.get().getStatus())) {
                throw new AppException(Constant.ErrorCode.WAITING_VALIDATE.name());
            }
        }
        Account newAccount = new Account();
        newAccount.setEmail(registerRequest.getEmail());
        newAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newAccount.setRole(Constant.Role.USER.name());
//            waiting for email cronjob
        newAccount.setStatus(Constant.TypeConstant.AccountStatus.NOT_VALIDATE);
//        newAccount.setStatus(Constant.TypeConstant.AccountStatus.ACTIVE);
        newAccount = accountRepo.save(newAccount);
        CreateContactRequest createContactRequest = registerRequest.getCreateContactRequest();
        Contact newContact = new Contact();

//        newContact.setAccount(newAccount);
        newContact.setEmailAccount(newAccount.getEmail());
        newContact.setContactType(Constant.TypeConstant.ContactType.INDIVIDUAL);
        newContact.setEmail(newAccount.getEmail());
        newContact.setPhone(createContactRequest.getPhone());
        newContact.setMobile(createContactRequest.getMobile());
        newContact.setWebsiteLink(createContactRequest.getWebsiteLink());
        newContact.setImagePath(createContactRequest.getImagePath());
        newContact.setFirstName(createContactRequest.getFirstName());
        contactRepo.save(newContact);
        return newAccount;

    }

    public Account changePassword(ChangePasswordRequest request) throws AppException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),
                            request.getPassword())
            );
        } catch (Exception e) {
            throw new AppException(e, Constant.ErrorCode.WRONG_PASSWORD.name());
        }
        Account account = accountRepo.getOne(request.getEmail());
        String newPassword = passwordEncoder.encode(request.getNewPassword());
        account.setPassword(newPassword);
        return accountRepo.save(account);
    }

    public String activeAccount(String token) {
        String email = JwtUtil.extractAllClaims(token).getSubject();
        Account account = accountRepo.findAccountByEmail(email).get();
        account.setStatus(Constant.TypeConstant.AccountStatus.ACTIVE);
        return domainWeb + "/activated";
    }
}
