package vo;

public class HouseVO {
    private Long id;
    private String address;
    private  Long city_id;

    public Long getCity_id() { return city_id; }

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
        //String x = "{id: " + getId() + ", name: " + getName() + ", population: " + getPopulation() + "}";
        String x  = "{address: " + getAddress() + ", city_id: " + getCity_id() + "}";
        return x;
    }
}
