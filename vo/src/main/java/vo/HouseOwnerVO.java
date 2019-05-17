package vo;

public class HouseOwnerVO {
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

    public String toString(){
        String x  = "{person_id: " + getPerson_id() + ", house_id: " + getHouse_id() + ", flat: " + getFlat() + "}";
        return x;
    }
}
