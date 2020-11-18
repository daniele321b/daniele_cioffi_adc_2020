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
    List<String> friends = new ArrayList<String>();

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
