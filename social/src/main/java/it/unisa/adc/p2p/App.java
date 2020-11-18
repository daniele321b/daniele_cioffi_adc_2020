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
                    Functions.getAnswers(peer.getUserProfileQuestions(), peer._answers);
                    System.out.println("OK GOOD");
                    Functions.showAnswers(peer._answers);
                    // create Key

                    // nickname
                    System.out.println("INSERT NICKNAME");
                    String nickName = input.next();
                    // join
                    peer.store("id" + args[0], nickName);
                    break;
                case 2:
                    // get firends
                    System.out.println("INSERT id to search");
                    String id = input.next();
                    System.out.println("Name:" + id + " Nick:" + peer.get(id));
                    // User user1 = peer.get(id);
                    // System.out.println("friend0 " + user1.friends.get(0));
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
        System.out.println("2 - ALTRO");
        System.out.println("0 - USCIRE");

    }

}