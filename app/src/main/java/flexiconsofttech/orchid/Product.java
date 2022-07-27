package flexiconsofttech.orchid;

public class Product {
    private String pid;
    private String name;
    private String photo;
    private String price;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Product(String pid, String name, String photo, String price) {
        this.pid = pid;
        this.name = name;
        this.photo = photo;
        this.price = price;
    }
}
