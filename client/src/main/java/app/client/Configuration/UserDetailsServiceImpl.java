package app.client.Configuration;

import UserJPA.Entities.UserE;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> grantList = new ArrayList<>();
        grantList.add(new SimpleGrantedAuthority("ROLE_USER"));

        try {
            URL url = new URL("http://user-service/users/getByUN/{username}");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            UserE userE = (UserE) con.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }


        UserDetails userDetails = (UserDetails) new User("aaa",
                "aaa", grantList);
        return userDetails;
    }
}
