import java.util.ArrayList;
import java.util.List;

public class CityNode {
    private int depth;
    private String cityName;
    private int cityId;
    private List<CityNode> children; // Alt düğümler
    private List<Cargo> cargos;      // Bu şehirdeki kargolar

    // Constructor
    public CityNode(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.children = new ArrayList<>();
        this.cargos = new ArrayList<>();
        this.depth = 0;
    }

    // Şehri ID ile bulma
    public CityNode findCityById(CityNode currentNode, int cityId) {
        if (currentNode == null) {
            return null;
        }

        if (currentNode.getCityId() == cityId) {
            return currentNode;
        }

        for (CityNode child : currentNode.getChildren()) {
            CityNode result = findCityById(child, cityId);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    // Getter ve Setter'lar
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

    // Alt düğüm ekle
    public void addChild(CityNode child) {
        child.setDepth(this.depth + 1); // Derinlik, ebeveynin derinliğinin bir fazlası
        this.children.add(child);
    }

    // Kargo ekle
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
