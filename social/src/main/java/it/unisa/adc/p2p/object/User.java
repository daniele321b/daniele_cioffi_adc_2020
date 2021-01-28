package it.unisa.adc.p2p.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    /*
     * This class describes all the basic characteristics of a social user. An
     * instance of this class will be populated by every peer present on the network
     * and will be inserted into the network to make available the information
     * contained in them. Therefore, both information referring directly to the user
     * and information useful for the system and its operation will be available.
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

    /*
     * This method allows you to return all user information as a single string.
     */
    public String printUser() {
        return "NICKNAME: " + getNickName() + "\nCHIAVE PROFILO: " + "*********" + "\nNAME: " + getName()
                + "\nSURNAME: " + getSurname() + "\nAGE: " + getAge();
    }

    public String printMe() {
        return "NICKNAME: " + getNickName() + "\nCHIAVE PROFILO: " + getProfileKey() + "\nNAME: " + getName()
                + "\nSURNAME: " + getSurname() + "\nAGE: " + getAge();
    }

}
