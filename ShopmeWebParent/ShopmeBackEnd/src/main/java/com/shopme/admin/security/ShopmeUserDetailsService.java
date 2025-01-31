package com.shopme.admin.security;


import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopmeUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user= userRepository.getUserByEmail(email);
       if(user!=null){
           return new ShopmeUserDetails(user);
       }

           throw new UsernameNotFoundException("Could Not Found user with email: "+email);
    }
}

