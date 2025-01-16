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

    public Client(int id, String name, String surname, LinkedList<Cargo> cargoHistory) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.cargoHistory = cargoHistory;
    }

    public void addCargoToClient(Cargo cargo) {
        // Tarihe göre ters sıralı bir şekilde ekleme (yeni tarihler önce)
        int index = 0;
        while (index < cargoHistory.size() && cargoHistory.get(index).getDate().before(cargo.getDate())) {
            index++;
        }
        cargoHistory.add(index, cargo);
    }

    // Son gönderilen 5 kargoyu stack ile sorgulama
    public void getLast5Cargos() {
        if (cargoHistory.isEmpty()) {
            System.out.println("Gönderim geçmişi boş!");
            return;
        }

        // Stack oluşturuluyor
        Stack<Cargo> stack = new Stack<>();

        // Son 5 kargoyu yığına ekleyelim
        int startIndex = Math.max(0, cargoHistory.size() - 5);
        for (int i = startIndex; i < cargoHistory.size(); i++) {
            stack.push(cargoHistory.get(i)); // Son 5 kargoyu stack'e ekle
        }

        // Yığındaki kargaları ekrana yazdıralım
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
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' ;
    }
}
