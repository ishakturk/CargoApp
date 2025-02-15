import java.util.Comparator;
import java.util.PriorityQueue;


public class CargoPriorityQueue {
    private PriorityQueue<Cargo> cargoQueue;
    private CityNode root;

    // We use a special comparator to sort the Priority Queue by delivery time.
    public CargoPriorityQueue(CityNode root) {
        this.root = root;
        this.cargoQueue = new PriorityQueue<>(Comparator.comparingInt(Cargo::getDeliveryTime));
    }

    // Adding cargo
    public void addCargo(Cargo cargo) {
        CityNode city = findCityById(root, cargo.getCityId()); // Şehri bul
        if (city == null) {
            System.out.println("Şehir bulunamadı. Kargo eklenemedi.");
            return;
        }

        // Adjust delivery time according to the depth of the tree
        cargo.setDeliveryTime(city.getDepth() + 1);
        cargoQueue.offer(cargo); // Kuyruğa ekle
        System.out.println("Kargo eklendi: ID=" + cargo.getId() + ", Süre=" + cargo.getDeliveryTime());
    }

    // Processing priority cargo
    public Cargo processNextCargo() {
        if (cargoQueue.isEmpty()) {
            System.out.println("İşlenecek kargo yok.");
            return null;
        }
        Cargo nextCargo = cargoQueue.poll(); // Receive priority shipping
        System.out.println("İşlenen kargo: ID=" + nextCargo.getId() + ", Süre=" + nextCargo.getDeliveryTime());
        return nextCargo;
    }

    // Find city by city ID (by navigating through the tree structure)
    private CityNode findCityById(CityNode node, int cityId) {
        if (node == null) return null;
        if (node.getCityId() == cityId) return node;

        for (CityNode child : node.getChildren()) {
            CityNode found = findCityById(child, cityId);
            if (found != null) return found;
        }
        return null;
    }
}

