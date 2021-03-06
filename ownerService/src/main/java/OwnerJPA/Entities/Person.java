package OwnerJPA.Entities;

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
    //private Long registered_at;

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

//    public Long getRegistered_at() {
//        return registered_at;
//    }
//
//    public void setRegistered_at(Long id) {
//        this.registered_at = id;
//    }

    public static Person fromVO(PersonVO personVO){
        Person person = new Person();
        person.setName(personVO.getName());
        person.setPassport(personVO.getPassport());
        //person.setRegistered_at(personVO.getRegistered_at());
        return person;
    }

    public String toLog(){
        String x  = "{id: " + getId() + ", name: " + getName() + ", passport: " + getPassport() + "}";
        return x;
    }

    public String toString(){
        return name;
    }
}
