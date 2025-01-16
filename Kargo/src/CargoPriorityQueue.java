import java.util.Comparator;
import java.util.PriorityQueue;

public class CargoPriorityQueue {
    private PriorityQueue<Cargo> cargoQueue;
    private CityNode root; // Ağacın kök düğümü

    // Priority Queue'yu teslimat süresine göre sıralamak için özel bir comparator kullanıyoruz.
    public CargoPriorityQueue(CityNode root) {
        this.root = root;
        this.cargoQueue = new PriorityQueue<>(Comparator.comparingInt(Cargo::getDeliveryTime));
    }

    // Kargo ekleme işlemi
    public void addCargo(Cargo cargo) {
        CityNode city = findCityById(root, cargo.getCityId()); // Şehri bul
        if (city == null) {
            System.out.println("Şehir bulunamadı. Kargo eklenemedi.");
            return;
        }

        // Teslimat süresini ağacın derinliğine göre ayarla
        cargo.setDeliveryTime(city.getDepth() + 1);
        cargoQueue.offer(cargo); // Kuyruğa ekle
        System.out.println("Kargo eklendi: ID=" + cargo.getId() + ", Süre=" + cargo.getDeliveryTime());
    }

    // Öncelikli kargoyu işleme alma
    public Cargo processNextCargo() {
        if (cargoQueue.isEmpty()) {
            System.out.println("İşlenecek kargo yok.");
            return null;
        }
        Cargo nextCargo = cargoQueue.poll(); // Öncelikli kargoyu al
        System.out.println("İşlenen kargo: ID=" + nextCargo.getId() + ", Süre=" + nextCargo.getDeliveryTime());
        return nextCargo;
    }

    // Kargo kuyruğunu yazdırma
    public void printQueue() {
        if (cargoQueue.isEmpty()) {
            System.out.println("Kargo kuyruğu boş.");
        } else {
            System.out.println("Kargo Kuyruğu (Teslimat Süresine Göre):");
            cargoQueue.forEach(cargo -> System.out.println(
                    "ID=" + cargo.getId() + ", Süre=" + cargo.getDeliveryTime() + ", Durum=" + cargo.getStatus()));
        }
    }

    // Şehir ID'ye göre şehri bulma (ağaç yapısında dolaşarak)
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

