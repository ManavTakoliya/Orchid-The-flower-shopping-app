package flexiconsofttech.orchid;

public class offers {

    private String oid,pid,discount,ophoto;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOphoto() {
        return ophoto;
    }

    public void setOphoto(String ophoto) {
        this.ophoto = ophoto;
    }

    public offers(String oid, String pid, String discount, String ophoto) {
        this.oid = oid;
        this.pid = pid;
        this.discount = discount;
        this.ophoto = ophoto;
    }
}
