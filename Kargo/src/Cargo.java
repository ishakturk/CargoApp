import java.util.Date;

public class Cargo {
    private int id;

    private Date date;

    private boolean isDelivered;

    private Status status;

    private int deliveryTime;

    private Client client;

    private int cityId;

    public Cargo(int id, Date date, int deliveryTime, Status status, Client client, int cityId){
        this.id = id;
        this.date = date;
        this.deliveryTime = deliveryTime;
        this.status = status;
        this.client = client;
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                ", date=" + date +
                ", isDelivered=" + isDelivered +
                ", status=" + status +
                ", deliveryTime=" + deliveryTime +
                ", client=" + client +
                '}';
    }
}
