package app.client.Configuration;

import UserJPA.Entities.UserE;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<GrantedAuthority> grantList = new ArrayList<>();
        grantList.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserE userE = externalRestTemplate.exchange("http://user-service/users/getByUN/{username}",
                HttpMethod.GET, null, new ParameterizedTypeReference<UserE>() {}, username).getBody();

        UserDetails userDetails = (UserDetails) new User(userE.getUsername(),
                    userE.getPassword(),
                    grantList);
            return userDetails;

//        try {
//            URL url = new URL("http://localhost:4443/users/getByUN/"+username);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//
//            UserE userE = (UserE) con.getContent();
//            UserDetails userDetails = (UserDetails) new User(userE.getUsername(),
//                    userE.getPassword(),
//                    grantList);
//            return userDetails;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        UserDetails userDetails = (UserDetails) new User("aaa",
//                "aaa", grantList);
//        return null;
    }
    @Primary
    @Bean
    private RestTemplate externalRestTemplate(){
        return new RestTemplate();
    }
}
