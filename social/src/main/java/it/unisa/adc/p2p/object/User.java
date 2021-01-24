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
    String name;
    String surname;
    int age;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String printUser() {
        return "NICKNAME: " + getNickName() + "\nCHIAVE PROFILO: " + getProfileKey() + "\nNAME: " + getName()
                + "\nSURNAME: " + getSurname() + "\nAGE: " + getAge();
    }

}
