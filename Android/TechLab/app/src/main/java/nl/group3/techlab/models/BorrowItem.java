package nl.group3.techlab.models;

import java.io.Serializable;
import java.util.Date;

public class BorrowItem implements Serializable {

    int id;
    Item item;
    User user;
    Date startDate;
    Date endDate;

    public BorrowItem(int id, Item item, User user, Date startDate, Date endDate){
        this.id = id;
        this.item = item;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId(){ return id; }

    public Item getItem() {
        return item;
    }

    public User getUser() {
        return user;
    }

    public Date getBorrowDate() {
        return startDate;
    }

    public Date getReturnDate() {
        return endDate;
    }
}
