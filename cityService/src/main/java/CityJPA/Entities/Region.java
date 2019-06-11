package CityJPA.Entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import vo.RegionVO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Where(clause = "deleted = 0")
@SQLDelete(sql = "update regions set deleted = 1 where id = ?")
@Entity(name = "regions")
public class Region {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;

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

    public static Region fromVO(RegionVO regionVO){
        Region region = new Region();
        region.setName(regionVO.getName());
        return region;
    }

//    public String toString(){
//        String x  = "{id: " + getId() + ", name: " + getName() + "}";
//        return x;
//    }

    public String toString() {
        return name;
    }
}
