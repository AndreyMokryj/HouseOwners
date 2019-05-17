package DaoJPA.Entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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

    //    public String getSite() {
//        return site;
//    }
//
//    public void setSite(String site) {
//        this.site = site;
//    }
    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long x) {
        this.population = x;
    }

    public String toString(){
        String x = "{id: " + getId() + ", name: " + getName() + ", population: " + getPopulation() + "}";

        return x;
    }
}
