package it.unisa.adc.p2p;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import it.unisa.adc.p2p.interfaces.MessageListener;
import it.unisa.adc.p2p.utility.Functions;
import it.unisa.adc.p2p.*;
import net.tomp2p.nat.PeerBuilderNAT;

public class App {
    protected static SocialImplementation peer;
    static Scanner input = new Scanner(System.in);
    static List<String> list = new ArrayList<String>();
    // come prova

    public static void main(String[] args) throws Exception, NumberFormatException {
        // final List<Integer> _answersList = new ArrayList<Integer>();
        peer = new SocialImplementation(Integer.parseInt(args[0]), "127.0.0.1", null);
        System.out.println("<<<-----------WELCOME TO SOCIAL----------->>>");
        System.out.println("Peer number ----> " + args[0]);
        // Functions.getAnswers(peer.getUserProfileQuestions(), _answersList);
        Functions.getAnswers(peer.getUserProfileQuestions(), peer._user.get_answers());
        Functions.showAnswers(peer._user.get_answers());
        String user = peer.createAuserProfileKey(peer._user.get_answers());
        System.out.println("Peer ProfileKey ----> " + user);
        String s = new String();
        s = input.nextLine();
        System.out.println(peer.join(user, s));

        int m;
        do {
            System.out.println("<<<-----------Menù----------->>>");
            System.out.println("<<<-----------1 cerca peer tramite Profile key----------->>>");
            System.out.println("<<<-----------2 visualizza amici----------->>>");
            m = input.nextInt();

            switch (m) {
                case 1:
                    System.out.println("Ora cerco!");
                    s = input.next();
                    System.out.println(peer.get(s).getNickName());
                    break;
                case 2:
                    System.out.println("Friends!");
                    list = peer.getFriends();
                    System.out.println(list);
                    // peer.getPeersObject();
                    break;
            }
        } while (m != 0);
    }

    public static void showString(List<String> lista) {
        System.out.println("FRIENDS");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(i + 1 + "- " + lista.get(i));
        }
    }

}