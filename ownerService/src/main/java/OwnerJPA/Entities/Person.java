package OwnerJPA.Entities;

import OwnerJPA.Repositories.PersonRepository;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import vo.PersonVO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "people")
@Where(clause = "deleted = 0")
@SQLDelete(sql = "update people set deleted = 1 where id = ?")
public class Person {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String passport;

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

    public static Person fromVO(PersonVO personVO){
        Person person = new Person();
        person.setName(personVO.getName());
        person.setPassport(personVO.getPassport());
        return person;
    }

    public String toString(){
        String x  = "{id: " + getId() + ", name: " + getName() + ", passport: " + getPassport() + "}";
        return x;
    }
}
