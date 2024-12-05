package rw.wetech.HospitalManagementSystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rw.wetech.HospitalManagementSystem.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class MyUserDetailService  implements UserDetailsService {

private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("This username is not found: " + username));

    }
}
