package model;

public class CityTree {
    private CityNode root;

    public CityTree(CityNode root) {
        this.root = root;
    }

    public CityNode getRoot() {
        return root;
    }

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

    public void printTree(CityNode currentNode, int depth) {
        if (currentNode == null) {
            return;
        }

        // Print the current node
        System.out.println("-".repeat(depth) + " " + currentNode.getCityName() + " (ID: " + currentNode.getCityId() + ")");
        for (Cargo cargo : currentNode.getCargos()) {
            System.out.println(" ".repeat(depth + 2) + "* Kargo ID: " + cargo.getId() + ", Durum: " + cargo.getStatus());
        }

        // Print child nodes
        for (CityNode child : currentNode.getChildren()) {
            printTree(child, depth + 2);
        }
    }

    public void addCargoToCity(int cityId, Cargo cargo) {
        CityNode city = findCityById(root, cityId);
        if (city != null) {
            city.addCargo(cargo);
        } else {
            System.out.println("Şehir ID'si bulunamadı: " + cityId);
        }
    }
}
