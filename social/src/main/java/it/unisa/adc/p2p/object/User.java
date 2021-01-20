package it.unisa.adc.p2p.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String nickName;
    String profileKey;
    List<String> friends = new ArrayList<String>();
    public List<Integer> _answers = new ArrayList<Integer>();

    public User() {

    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<Integer> get_answers() {
        return _answers;
    }

    public void set_answers(List<Integer> _answers) {
        this._answers = _answers;
    }

    public void printUser() {
        System.out.println("NICKNAME: " + getNickName());
        System.out.println("CHIAVE PROFILO: " + getProfileKey());
    }
}
