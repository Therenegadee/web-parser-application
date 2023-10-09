package ru.researchser.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.researchser.DTOs.UserDTO;
import ru.researchser.mappers.UserMapper;
import ru.researchser.models.user.User;
import ru.researchser.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: %s Not Found" + username));
        return user;
    }

    @Transactional
    public UserDTO getUserById(Long id){
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(id)));
        return userMapper.toDTO(user);
    }

    @Transactional
    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository
                .findAll();
        return userMapper.toDTO(users);
    }

    @Transactional
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: %s Not Found" + email));
        return user;
    }
}
