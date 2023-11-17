package com.nt.rookies.assets.securities;

import com.nt.rookies.assets.entities.User;
import com.nt.rookies.assets.exceptions.NotFoundException;
import com.nt.rookies.assets.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class UserDetailsServiceImpl implements UserDetailsService  {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username: " + username + " not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Set.of(new SimpleGrantedAuthority(user.getRole().name())));
    }

}
