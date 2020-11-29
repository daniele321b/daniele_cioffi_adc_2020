package it.unisa.adc.p2p;

import java.util.*;

/**
 * Hello world!
 */
public final class App {
    protected static SocialImplementation peer;

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception, NumberFormatException {

        peer = new SocialImplementation(Integer.parseInt(args[0]), "127.0.0.1");
        String cmd = args[0];
        System.out.println("<<<-----------WELCOME TO SOCIAL----------->>>");
        switchCase(cmd);
        // execution(cmd);
    }

    public static void Menu() {
        System.out.println("<-----------MENU--------->");
        System.out.println("1 - ACCESSO AL SOCIAL");
        System.out.println("2 - GET USERNAME");
        System.out.println("3 - WE ARE FRIEND?");
        System.out.println("4 - SEARCH FRIEND?");
        System.out.println("5 - GET FRIEND?");
        System.out.println("0 - USCIRE");

    }

    public static void execution(String cmd) throws Exception, NumberFormatException {

        Functions.getAnswers(peer.getUserProfileQuestions(), peer._user._answers);
        System.out.println("OK GOOD");
        Functions.showAnswers(peer._user._answers);
        System.out.println("INSERT NICKNAME");
        String nickName = input.next();
        peer.store("id" + cmd, nickName);
        String s2 = peer.createAuserProfileKey(peer._user._answers);
        System.out.println("UserProfileKey: " + s2);
        while (true) {
            FunctionsPeer.searchFriend(peer);
            peer.getFriends();
        }

    }

    public static void switchCase(String cmd) throws Exception, NumberFormatException {
        while (true) {
            Menu();
            int in = input.nextInt();

            switch (in) {
                case 1:
                    // get User profile questions
                    Functions.getAnswers(peer.getUserProfileQuestions(), peer._user._answers);
                    System.out.println("OK GOOD");
                    Functions.showAnswers(peer._user._answers);
                    // create User profile Key

                    // nickname
                    System.out.println("INSERT NICKNAME");
                    String nickName = input.next();
                    // join
                    // peer.store("id" + args[0], nickName);
                    // User new_user = new User();
                    // new_user.userName = nickName;
                    // STOREEEEEEEE
                    peer.store("id" + cmd, nickName);
                    String s2 = peer.createAuserProfileKey(peer._user._answers);
                    System.out.println("UserProfileKey: " + s2);
                    break;
                case 2:
                    // get firends
                    System.out.println("INSERT ID TO SEARCH");
                    String id0 = input.next();
                    // System.out.println("Name:" + id0 + " Nick:" + peer.get(id0));
                    // User user0 = peer.get(id0);
                    // System.out.println("userName " + user0.userName);

                    try {
                        String s1 = peer.getUsername(id0);
                        System.out.println("userName: " + s1);
                    } catch (NoSuchElementException elementException) {
                        System.out.println("NO ID");
                    }

                    break;
                case 3:
                    System.out.println("INSERT TO VERIFY FRIEND");
                    String id1 = input.next();
                    User user1 = peer.get(id1);
                    // boolean x = Functions.weAreFriends(peer._user, user1);
                    System.out.println("Stringhe da conforntare \n ");
                    Functions.showAnswers(peer._user._answers);
                    try {
                        Functions.showAnswers(user1._answers);
                        if (Functions.weAreFriends(peer._user, user1)) {
                            System.out.println(peer._user.userName + " AND " + user1.userName + "Are friend...");

                        } else {
                            System.out.println(peer._user.userName + " AND " + user1.userName + "Are Not friend...");
                        }
                    } catch (NoSuchElementException elementException) {
                        System.out.println("NO ID");
                    }

                    break;
                case 4:
                    FunctionsPeer.searchFriend(peer);
                    break;
                case 5:
                    Functions.showFriends(peer.getFriends());
                    break;

                default:
                    break;
            }

        }

    }

}