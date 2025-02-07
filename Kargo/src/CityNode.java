import java.util.ArrayList;
import java.util.List;

public class CityNode {
    private int depth;
    private String cityName;
    private int cityId;
    private List<CityNode> children;
    private List<Cargo> cargos;

    public CityNode(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.children = new ArrayList<>();
        this.cargos = new ArrayList<>();
        this.depth = 0;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public List<CityNode> getChildren() {
        return children;
    }

    public List<Cargo> getCargos() {
        return cargos;
    }


    public void addChild(CityNode child) {
        child.setDepth(this.depth + 1); // Depth, one more than the depth of the parent
        this.children.add(child);
    }

    public void addCargo(Cargo cargo) {
        this.cargos.add(cargo);
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
