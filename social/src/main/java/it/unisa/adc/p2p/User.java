package it.unisa.adc.p2p;

import java.util.ArrayList;
import java.util.List;

public class User {
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
