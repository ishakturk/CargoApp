import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class CargoManager {
    private CargoPriorityQueue priorityQueue; // Öncelik kuyruğu
    private CityTree cityTree;               // Rota ağacı

    public CargoManager(CityTree cityTree) {
        this.cityTree = cityTree;
        this.priorityQueue = new CargoPriorityQueue(cityTree.getRoot());
    }

    // Kargoyu sisteme ekle
    public void addCargo(Cargo cargo, int cityId, Client client) {
        // Öncelik kuyruğuna ekle
        priorityQueue.addCargo(cargo);

        // İlgili şehre ekle
        cityTree.addCargoToCity(cityId, cargo);

        cargo.setCityId(cityId);

        // Müşteriye kargoyu ekle
        client.addCargoToClient(cargo);
    }

    // Öncelikli kargoyu işleme al
    public Cargo processNextCargo() {
        // Öncelik kuyruğundan al
        Cargo nextCargo = priorityQueue.processNextCargo();

        if (nextCargo != null) {
            // Şehri bul ve durumunu güncelle
            CityNode city = cityTree.findCityById(cityTree.getRoot(), findCityIdByCargo(nextCargo));
            if (city != null) {
                nextCargo.setStatus(Status.DELIVERED);
            }
        }
        return nextCargo;
    }

    // Ağacı yazdır
    public void printRoutes() {
        cityTree.printTree(cityTree.getRoot(), 0);
    }

    // Helper: Kargo hangi şehirde?
    private int findCityIdByCargo(Cargo cargo) {
        return findCityIdRecursive(cityTree.getRoot(), cargo);
    }

    private int findCityIdRecursive(CityNode node, Cargo cargo) {
        if (node == null) return -1;

        for (Cargo c : node.getCargos()) {
            if (c.getId() == cargo.getId()) {
                return node.getCityId();
            }
        }

        for (CityNode child : node.getChildren()) {
            int result = findCityIdRecursive(child, cargo);
            if (result != -1) {
                return result;
            }
        }
        return -1;
    }

    // Tüm kargoları listeye ekleme
    public List<Cargo> getAllCargos(List<Cargo> cargos) {

        // Tüm kargoları al
        List<Cargo> allCargos = new ArrayList<>(cargos);

        // Kargoları ID'ye göre sıralama
        allCargos.sort(Comparator.comparingInt(Cargo::getId));

        return allCargos;
    }

    // Teslim edilmiş kargoları listeye ekleme
    public List<Cargo> getDeliveredCargos(List<Cargo> cargos) {
        List<Cargo> deliveredCargos = new ArrayList<>();

        // Teslim edilmiş kargoları filtrele
        for (Cargo cargo : cargos) {
            if (cargo.getStatus() == Status.DELIVERED) {
                deliveredCargos.add(cargo);
            }
        }

        // Teslim edilen kargoların ID'lerine göre sıralama (Binary Search için sıralama)
        // Sort Method Time Complexity(O(nlogn))
        deliveredCargos.sort(Comparator.comparingInt(Cargo::getId));

        return deliveredCargos;
    }

    // Teslim edilmemiş kargoları listeye ekleme
    public List<Cargo> getUndeliveredCargos(List<Cargo> cargos) {
        List<Cargo> undeliveredCargos = new ArrayList<>();

        // Teslim edilmemiş kargoları filtrele
        for (Cargo cargo : cargos) {
            // Status null değilse ve teslim edilmemişse (DELIVERED dışındaki durumlar)
            if (cargo.getStatus() != null && cargo.getStatus() != Status.DELIVERED) {
                undeliveredCargos.add(cargo);
            }
        }

        // Teslim edilmemiş kargoları teslimat sürelerine göre sıralama (Merge Sort)
        mergeSortById(undeliveredCargos);

        return undeliveredCargos;
    }


    // ID'ye göre Merge Sort algoritması (Time Complexity O(nlogn))
    private void mergeSortById(List<Cargo> cargos) {
        if (cargos == null || cargos.size() <= 1) {
            return;
        }
        int mid = cargos.size() / 2;
        List<Cargo> left = new ArrayList<>(cargos.subList(0, mid));
        List<Cargo> right = new ArrayList<>(cargos.subList(mid, cargos.size()));
        mergeSortById(left);
        mergeSortById(right);
        mergeById(cargos, left, right);
    }

    // ID'ye göre Merge işlemi
    private void mergeById(List<Cargo> cargos, List<Cargo> left, List<Cargo> right) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getId() < right.get(j).getId()) {
                cargos.set(k++, left.get(i++));
            } else {
                cargos.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            cargos.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            cargos.set(k++, right.get(j++));
        }
    }


    // Binary Search ile kargo arama. Time complexity (O(logn))
    public Cargo binarySearchById(List<Cargo> cargos, int id) {
        int left = 0;
        int right = cargos.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (cargos.get(mid).getId() == id) {
                return cargos.get(mid);
            } else if (cargos.get(mid).getId() < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return null; // Kargo bulunamazsa
    }
}
