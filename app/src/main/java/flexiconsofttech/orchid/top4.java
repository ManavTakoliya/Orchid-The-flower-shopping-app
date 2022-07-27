package flexiconsofttech.orchid;

public class top4 {
    private String tid;
    private String tphoto;
    private String tname;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTphoto() {
        return tphoto;
    }

    public void setTphoto(String tphoto) {
        this.tphoto = tphoto;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public top4(String tid, String tphoto, String tname) {

        this.tid = tid;
        this.tphoto = tphoto;
        this.tname = tname;
    }
}
