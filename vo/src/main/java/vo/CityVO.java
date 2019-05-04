package vo;

public class CityVO {
    private Long id;
    private String name;
    //private String site;
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
        //String x = "{id: " + getId() + ", name: " + getName() + ", population: " + getPopulation() + "}";
        String x  = "{name: " + getName() + ", population: " + getPopulation() + "}";
        return x;
    }
}
