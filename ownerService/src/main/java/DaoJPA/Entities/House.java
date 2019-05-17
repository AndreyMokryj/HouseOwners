package DaoJPA.Entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "houses")
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update houses set deleted = 1 where id = ?")
public class House {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String address;
    //private double price;
    private  Long city_id;

    public Long getCity_id() {
        return city_id;
    }

    public void setCity_id(Long id) {
        this.city_id = id;
    }


    public String getAddress() {
        return address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString(){
        String x = "{id: " + getId() + ", address: " + getAddress() + ", city_id: " + getCity_id() + "}";

        return x;
    }
}
