package UserJPA.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "users")
public class UserE {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private int isadmin;

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

    public int getIsadmin(){
        return isadmin;
    }

    public boolean admin(){
        return isadmin > 0;
    }

    public void setIsadmin(int isadmin){
        this.isadmin = isadmin;
    }
}
