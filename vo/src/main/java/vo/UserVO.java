package vo;

public class UserVO {
    private Long id;
    private String username;
    private String password;
    private boolean isadmin;

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

    public boolean getIsadmin(){ return isadmin;}

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public String toString(){
        String x  = "{id: " + getId() + ", username: " + getUsername() + ", password: " + getPassword() + ", isadmin: " + getIsadmin() + "}";
        return x;
    }
}
