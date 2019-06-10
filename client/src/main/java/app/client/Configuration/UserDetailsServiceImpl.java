package app.client.Configuration;

import UserJPA.Entities.UserE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private RestTemplate externalRestTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Iterable<String> roles =  externalRestTemplate.exchange("http://user-service/users/getRoles/{username}",
                HttpMethod.GET, null, new ParameterizedTypeReference<Iterable<String>>() {}, username).getBody();
        List<GrantedAuthority> grantList = new ArrayList<>();

        for (String role:
             roles) {
            grantList.add(new SimpleGrantedAuthority(role));
        }
        //grantList.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserE userE = externalRestTemplate.exchange("http://user-service/users/getByUN/{username}",
                HttpMethod.GET, null, new ParameterizedTypeReference<UserE>() {}, username).getBody();

        UserDetails userDetails = (UserDetails) new User(userE.getUsername(),
                    userE.getPassword(),
                    grantList);
        return userDetails;
    }

    @Primary
    @LoadBalanced
    @Bean
    private RestTemplate externalRestTemplate(){
        return new RestTemplate();
    }
}
