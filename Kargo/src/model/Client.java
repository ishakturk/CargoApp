package model;

import java.util.LinkedList;
import java.util.Stack;

public class Client {
    private int id;
    private String name;
    private String surname;
    private LinkedList<Cargo> cargoHistory;

    public Client(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.cargoHistory = new LinkedList<>();
    }

    public void addCargoToClient(Cargo cargo) {
        // Insert in reverse order by date (new dates first)
        int index = 0;
        while (index < cargoHistory.size() && cargoHistory.get(index).getDate().before(cargo.getDate())) {
            index++;
        }
        cargoHistory.add(index, cargo);
    }

    // Querying the last 5 shipments with stack
    public void getLast5Cargos() {
        if (cargoHistory.isEmpty()) {
            System.out.println("Gönderim geçmişi boş!");
            return;
        }

        // Creating a stack
        Stack<Cargo> stack = new Stack<>();

        // Add the last 5 cargoes to the stack
        int startIndex = Math.max(0, cargoHistory.size() - 5);
        for (int i = startIndex; i < cargoHistory.size(); i++) {
            stack.push(cargoHistory.get(i)); // Son 5 kargoyu stack'e ekle
        }

        // Print the crows in the stack on the screen
        System.out.println("Son 5 Kargo:");
        while (!stack.isEmpty()) {
            System.out.println(stack.pop()); // Son eklenen kargo önce çıkacak
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LinkedList<Cargo> getCargoHistory() {
        return cargoHistory;
    }

    public void setCargoHistory(LinkedList<Cargo> cargoHistory) {
        this.cargoHistory = cargoHistory;
    }

    @Override
    public String toString() {
        return "model.Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' ;
    }
}
