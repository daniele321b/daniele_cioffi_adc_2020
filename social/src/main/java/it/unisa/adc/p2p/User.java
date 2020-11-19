package it.unisa.adc.p2p;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String userName;
    String value;
    int id;
    List<String> friends = new ArrayList<String>();
    public List<Integer> _answers = new ArrayList<Integer>();

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
