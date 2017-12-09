/**
 * Created by qiujiarong on 02/12/2017.
 */
public class MessageRelation {
    String user;
    String contact;
    Relation relation;

    public MessageRelation(String user, String contact, Relation relation) {
        this.user = user;
        this.contact = contact;
        this.relation = relation;
    }

    public String getUser() {
        return user;
    }

    public String getContact() {
        return contact;
    }

    public Relation getRelation() {
        return relation;
    }

    public void update(){

    }
}
