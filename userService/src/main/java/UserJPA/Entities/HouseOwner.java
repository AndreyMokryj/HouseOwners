package UserJPA.Entities;

import UserJPA.Repositories.HouseOwnerRepository;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import vo.HouseOwnerVO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "house_owners")
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update house_owners set deleted = 1 where id = ?")
public class HouseOwner {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long person_id;
    private Long house_id;
    private int flat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long id) {
        this.person_id = id;
    }

    public Long getHouse_id() {
        return house_id;
    }

    public void setHouse_id(Long id) {
        this.house_id = id;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }

    public static HouseOwner fromVO(HouseOwnerVO houseOwnerVO){
        HouseOwner houseOwner = new HouseOwner();
        houseOwner.setPerson_id(houseOwnerVO.getPerson_id());
        houseOwner.setHouse_id(houseOwnerVO.getHouse_id());
        houseOwner.setFlat(houseOwnerVO.getFlat());
        return houseOwner;
    }

    public static void softDeleteById(HouseOwnerRepository repository, long id){
        repository.deleteById(id);
    }

    public String toString(){
        String x  = "{id: " + getId() + "person_id: " + getPerson_id() + ", house_id: " + getHouse_id() + ", flat: " + getFlat() + "}";
        return x;
    }
}
