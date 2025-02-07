import java.text.SimpleDateFormat;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // A HashMap to store users (ID -> Client)
        Map<Integer, Client> userDatabase = new HashMap<>();

        // Create a city tree
        CityNode central = new CityNode("ANKARA", 1);
        CityNode cityA = new CityNode("İSTANBUL", 2);
        CityNode cityB = new CityNode("BURSA", 3);
        CityNode cityC = new CityNode("İZMİR", 4);
        CityNode cityD = new CityNode("ANTALYA", 5);

        central.addChild(cityA);
        central.addChild(cityB);
        cityA.addChild(cityC);
        cityB.addChild(cityD);

        CityTree cityTree = new CityTree(central);

        // Create CargoManager
        CargoManager cargoManager = new CargoManager(cityTree);

        Client currentClient = null;

        while (true) {
            System.out.println("\n--- Kargo Sistemi ---");
            System.out.println("1. Kayıt Ol");
            System.out.println("2. Giriş Yap");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {
                case 1: // User Registration
                    System.out.print("Adınız: ");
                    String name = scanner.nextLine();
                    System.out.print("Soyadınız: ");
                    String surname = scanner.nextLine();
                    System.out.print("Kullanıcı ID (benzersiz): ");
                    int newUserId = scanner.nextInt();
                    scanner.nextLine();

                    if (userDatabase.containsKey(newUserId)) {
                        System.out.println("Bu ID zaten kullanılıyor. Lütfen başka bir ID seçin.");
                    } else {
                        Client newUser = new Client(newUserId, name, surname);
                        userDatabase.put(newUserId, newUser);
                        System.out.println("Kayıt başarılı! Kullanıcı ID: " + newUserId);
                    }
                    break;

                case 2: // User Login
                    System.out.print("Kullanıcı ID: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();

                    if (!userDatabase.containsKey(userId)) {
                        System.out.println("Bu ID ile kayıtlı bir kullanıcı bulunamadı.");
                    } else {
                        currentClient = userDatabase.get(userId);
                        System.out.println("Hoşgeldiniz, " + currentClient.getName() + " " + currentClient.getSurname());
                        handleUserOperations(scanner, currentClient, cargoManager, cityTree);
                    }
                    break;

                case 0: // Exit
                    System.out.println("Çıkış yapılıyor...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }
    }

    private static void handleUserOperations(Scanner scanner, Client currentClient, CargoManager cargoManager, CityTree cityTree) {
        int choice;
        do {
            System.out.println("\nLütfen bir seçim yapın:");
            System.out.println("1. Kargo ekle");
            System.out.println("2. Kargolarımı listele");
            System.out.println("3. ID'ye göre kargo ara");
            System.out.println("4. Teslim edilen kargoları ara");
            System.out.println("5. Teslim edilmeyen kargoları ara");
            System.out.println("6. Kargo rotalarını yazdır");
            System.out.println("7. Öncelikli kargo işleme al");
            System.out.println("8. Şehir ağacını yazdır");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Kargo ID: ");
                    int cargoId = scanner.nextInt();

                    System.out.print("Kargo tarihi (yyyy-MM-dd): ");
                    String dateStr = scanner.next();
                    Date date;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    } catch (Exception e) {
                        System.out.println("Hatalı tarih formatı.");
                        break;
                    }

                    System.out.print("Durum (1: PROCESSED, 2: ONDELIVERY, 3: DELIVERED): ");
                    int statusChoice = scanner.nextInt();
                    Status status = Status.values()[statusChoice - 1];

                    // Şehir Seçimi Ekranı
                    System.out.println("Mevcut Şehirler:");
                    printCities(cityTree.getRoot());

                    System.out.print("Şehir ID: ");
                    int cityId = scanner.nextInt();

                    CityNode cityNode = cityTree.findCityById(cityTree.getRoot(), cityId);
                    if (cityNode == null) {
                        System.out.println("Şehir ID bulunamadı.");
                        break;
                    }

                    int deliveryTime = cityNode.getDepth() + 1; // Teslim süresini hesapla

                    Cargo newCargo = new Cargo(cargoId, date, deliveryTime, status, currentClient, cityId);
                    cargoManager.addCargo(newCargo, cityId, currentClient);

                    System.out.println("Kargo başarıyla eklendi. Teslim süresi (gün): " + newCargo.getDeliveryTime());
                    break;

                case 2:
                    System.out.println("Son 5 Kargonuz:");
                    currentClient.getLast5Cargos();
                    break;

                case 3:
                    System.out.print("Aranacak kargo ID: ");
                    int searchId = scanner.nextInt();
                    List<Cargo> allCargos = currentClient.getCargoHistory();
                    Cargo foundCargo = cargoManager.binarySearchById(allCargos, searchId);
                    if (foundCargo != null) {
                        System.out.println("Bulunan kargo: " + foundCargo);
                    } else {
                        System.out.println("Kargo bulunamadı.");
                    }
                    break;

                case 4:
                    System.out.println("Teslim edilen kargolar:");
                    List<Cargo> deliveredCargos = cargoManager.getDeliveredCargos(currentClient.getCargoHistory());
                    for (Cargo cargo : deliveredCargos) {
                        System.out.println(cargo);
                    }
                    break;

                case 5:
                    System.out.println("Teslim edilmeyen kargolar:");
                    List<Cargo> undeliveredCargos = cargoManager.getUndeliveredCargos(currentClient.getCargoHistory());
                    for (Cargo cargo : undeliveredCargos) {
                        System.out.println(cargo);
                    }
                    break;

                case 6:
                    System.out.println("Kargo Rotaları:");
                    cargoManager.printRoutes();
                    break;

                case 7:
                    System.out.println("Öncelikli kargo işleniyor:");
                    Cargo processed = cargoManager.processNextCargo();
                    if (processed != null) {
                        System.out.println("İşlenen Kargo ID: " + processed.getId() + ", Yeni Durum: " + processed.getStatus());
                    } else {
                        System.out.println("İşlenecek kargo bulunamadı.");
                    }
                    break;

                case 8:
                    System.out.println("Şehir Ağacı:");
                    cargoManager.printRoutes();
                    break;

                case 0:
                    System.out.println("Geri dönülüyor...");
                    break;

                default:
                    System.out.println("Geçersiz seçim.");
            }
        } while (choice != 0);
    }

    private static void printCities(CityNode root) {
        if (root == null) return;
        Queue<CityNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            CityNode current = queue.poll();
            System.out.println("ID: " + current.getCityId() + ", Ad: " + current.getCityName());
            queue.addAll(current.getChildren());
        }
    }
}


