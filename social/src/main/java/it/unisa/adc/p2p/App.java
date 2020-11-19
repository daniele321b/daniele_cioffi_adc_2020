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

        while (true) {
            Menu();
            int s = input.nextInt();

            switch (s) {
                case 1:
                    // get User profile questions
                    Functions.getAnswers(peer.getUserProfileQuestions(), peer._user._answers);
                    System.out.println("OK GOOD");
                    Functions.showAnswers(peer._user._answers);
                    // create Key

                    // nickname
                    System.out.println("INSERT NICKNAME");
                    String nickName = input.next();
                    // join
                    // peer.store("id" + args[0], nickName);
                    // User new_user = new User();
                    // new_user.userName = nickName;
                    // STOREEEEEEEE
                    peer.store("id" + args[0], nickName);
                    break;
                case 2:
                    // get firends
                    System.out.println("INSERT ID TO SEARCH");
                    String id0 = input.next();
                    // System.out.println("Name:" + id0 + " Nick:" + peer.get(id0));
                    // User user0 = peer.get(id0);
                    // System.out.println("userName " + user0.userName);
                    String s1 = peer.getUsername(id0);
                    System.out.println("userName: " + s1);
                    break;
                case 3:
                    System.out.println("INSERT TO VERIFY FRIEND");
                    String id1 = input.next();
                    User user1 = peer.get(id1);
                    // boolean x = Functions.weAreFriends(peer._user, user1);
                    System.out.println("Stringhe da conforntare \n ");
                    Functions.showAnswers(peer._user._answers);
                    Functions.showAnswers(user1._answers);
                    if (Functions.weAreFriends(peer._user, user1)) {
                        System.out.println(peer._user.userName + " AND " + user1.userName + "Are friend...");

                    } else {
                        System.out.println(peer._user.userName + " AND " + user1.userName + "Are Not friend...");
                    }
                    break;

                default:
                    break;
            }

        }
        // if (args.length == 3) {
        // peer.store(args[1], args[2]);
        // }
        // if (args.length == 2) {
        // System.out.println("Name:" + args[1] + " Messaggio:" + peer.get(args[1]));
        // }

    }

    public static void Menu() {
        System.out.println("WELCOME TO SOCIAL");
        System.out.println("<-----------MENU--------->");
        System.out.println("1 - ACCESSO AL SOCIAL");
        System.out.println("2 - GET USERNAME");
        System.out.println("3 - WE ARE FRIEND?");
        System.out.println("0 - USCIRE");

    }

}