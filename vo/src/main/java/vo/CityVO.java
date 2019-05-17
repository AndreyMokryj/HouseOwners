package vo;

public class CityVO {
    private Long id;
    private String name;
    private Long population;
    private Long region_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }

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

    public String toString(){
        //String x = "{id: " + getId() + ", name: " + getName() + ", population: " + getPopulation() + "}";
        String x  = "{name: " + getName() + ", region_id: " + getRegion_id() + ", population: " + getPopulation() + "}";
        return x;
    }
}
