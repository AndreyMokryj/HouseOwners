package UserJPA.Entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import vo.CityVO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Where(clause = "deleted = 0")
@SQLDelete(sql = "update cities set deleted = 1 where id = ?")
@Entity(name = "cities")
public class City {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long population;
    private Long region_id;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long x) {
        this.population = x;
    }

    public Long getRegion_id() {
        return region_id;
    }

    public void setRegion_id(Long region_id) {
        this.region_id = region_id;
    }

    public static City fromVO(CityVO cityVO){
        City city = new City();
        city.setName(cityVO.getName());
        city.setPopulation(cityVO.getPopulation());
        city.setRegion_id(cityVO.getRegion_id());
        return city;
    }

    public String toString(){
        //String x = "{id: " + getId() + ", name: " + getName() + ", population: " + getPopulation() + "}";
        String x  = "{id: " + getId() + ",name: " + getName() + ", region_id: " + getRegion_id() + ", population: " + getPopulation() + "}";
        return x;
    }
}
