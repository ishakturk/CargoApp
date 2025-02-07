import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CargoManager {
    private CargoPriorityQueue priorityQueue;
    private CityTree cityTree;

    public CargoManager(CityTree cityTree) {
        this.cityTree = cityTree;
        this.priorityQueue = new CargoPriorityQueue(cityTree.getRoot());
    }

    // Add the cargo to the system
    public void addCargo(Cargo cargo, int cityId, Client client) {
        // Add to priority queue
        priorityQueue.addCargo(cargo);

        // Add to related city
        cityTree.addCargoToCity(cityId, cargo);

        cargo.setCityId(cityId);

        // Add shipment to customer
        client.addCargoToClient(cargo);
    }

    // Process priority shipment
    public Cargo processNextCargo() {
        // Get from priority queue
        Cargo nextCargo = priorityQueue.processNextCargo();

        if (nextCargo != null) {
            // Find your city and update your status
            CityNode city = cityTree.findCityById(cityTree.getRoot(), findCityIdByCargo(nextCargo));
            if (city != null) {
                nextCargo.setStatus(Status.DELIVERED);
            }
        }
        return nextCargo;
    }

    // Print the tree
    public void printRoutes() {
        cityTree.printTree(cityTree.getRoot(), 0);
    }

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

    // Add delivered shipments to the list
    public List<Cargo> getDeliveredCargos(List<Cargo> cargos) {
        List<Cargo> deliveredCargos = new ArrayList<>();

        // Filter delivered shipments
        for (Cargo cargo : cargos) {
            if (cargo.getStatus() == Status.DELIVERED) {
                deliveredCargos.add(cargo);
            }
        }

        // Sorting by IDs of delivered cargo (Sorting for Binary Search)
        // Sort Method Time Complexity(O(nlogn))
        deliveredCargos.sort(Comparator.comparingInt(Cargo::getId));

        return deliveredCargos;
    }

    // Add undelivered shipments to the list
    public List<Cargo> getUndeliveredCargos(List<Cargo> cargos) {
        List<Cargo> undeliveredCargos = new ArrayList<>();

        // Filter undelivered shipments
        for (Cargo cargo : cargos) {
            // If status is not null and not delivered (except DELIVERED)
            if (cargo.getStatus() != null && cargo.getStatus() != Status.DELIVERED) {
                undeliveredCargos.add(cargo);
            }
        }

        // Sort undelivered cargo by delivery times (Merge Sort)
        mergeSortById(undeliveredCargos);

        return undeliveredCargos;
    }


    // Merge Sort algorithm based on ID (Time Complexity O(nlogn))
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

    // Merge operation by ID
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


    // Searching cargo with Binary Search. Time complexity (O(logn))
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

        return null; // If cargo is not found
    }
}
