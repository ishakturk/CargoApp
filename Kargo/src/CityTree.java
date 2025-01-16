import java.util.ArrayList;
import java.util.List;

public class CityTree {
    private CityNode root;

    // Constructor
    public CityTree(CityNode root) {
        this.root = root;
    }

    // Root getter
    public CityNode getRoot() {
        return root;
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

    // Ağacı yazdırma
    public void printTree(CityNode currentNode, int depth) {
        if (currentNode == null) {
            return;
        }

        // Mevcut düğümü yazdır
        System.out.println("-".repeat(depth) + " " + currentNode.getCityName() + " (ID: " + currentNode.getCityId() + ")");
        for (Cargo cargo : currentNode.getCargos()) {
            System.out.println(" ".repeat(depth + 2) + "* Kargo ID: " + cargo.getId() + ", Durum: " + cargo.getStatus());
        }

        // Çocuk düğümleri yazdır
        for (CityNode child : currentNode.getChildren()) {
            printTree(child, depth + 2);
        }
    }

    // Kargoyu ilgili şehre ekleme
    public void addCargoToCity(int cityId, Cargo cargo) {
        CityNode city = findCityById(root, cityId);
        if (city != null) {
            city.addCargo(cargo);
        } else {
            System.out.println("Şehir ID'si bulunamadı: " + cityId);
        }
    }
}
