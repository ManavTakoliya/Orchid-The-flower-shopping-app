package flexiconsofttech.orchid;

public class Cart {

    private String cid;
    private String name;
    private String photo;
    private String price;
    private String quantity;
    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }



    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    private String productid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Cart(String cid,String productid, String name, String photo, String price, String quantity) {

        this.cid = cid;
        this.name = name;
        this.photo = photo;
        this.price = price;
        this.quantity = quantity;
        this.productid = productid;
    }
}
