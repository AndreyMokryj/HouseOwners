package vo;

public class PersonVO {
    private Long id;
    private String name;
    private String passport;
    private long registered_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Long getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(Long id) {
        this.registered_at = id;
    }

    public String toString(){
        String x  = "{name: " + getName() + ", passport: " + getPassport()+ ", registered_at: " + getRegistered_at() + "}";
        return x;
    }
}
