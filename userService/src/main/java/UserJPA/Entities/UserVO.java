package UserJPA.Entities;


import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class UserVO {
    private Long id;
    private String username;
    private String password;
    private Set<GrantedAuthority> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passport) {
        this.password = passport;
    }

//    public Set<RoleEnum> getRoles(){
//        return roles;
//    }
//
//    public void addRole(RoleEnum role){
//        roles.add(role);
//    }
//
//    public void removeRole(RoleEnum role){
//        if (roles.contains(role))
//            roles.remove(role);
//    }
}
