package vo;

public class RegionVO {
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

    public String toString(){
        String x  = "{id: " + getId() + ", name: " + getName() + "}";
        return x;
    }
}
