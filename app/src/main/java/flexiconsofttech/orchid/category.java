package flexiconsofttech.orchid;

public class category {

    private String id,name,photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public category(String id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }
}
