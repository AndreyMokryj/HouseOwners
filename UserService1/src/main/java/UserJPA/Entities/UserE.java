package UserJPA.Entities;

import vo.UserVO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Where(clause = "deleted = 0")
//@SQLDelete(sql = "update regions set deleted = 1 where id = ?")
@Entity(name = "users")
public class UserE {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

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

    public void setPassword(String name) {
        this.password = name;
    }

    public static UserE fromVO(UserVO userVO){
        UserE user = new UserE();
        user.setUsername(userVO.getUsername());
        user.setPassword(userVO.getPassword());
        return user;
    }

    public String toString(){
        String x  = "{id: " + getId() + ", username: " + getUsername() + ", password: " + getPassword() + "}";
        return x;
    }
}
