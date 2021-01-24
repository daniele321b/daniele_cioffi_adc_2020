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

public class App3 {
    protected static SocialImplementation peer;
    static Scanner input = new Scanner(System.in);
    static List<String> list = new ArrayList<String>();

    public static void main(String[] args) throws Exception, NumberFormatException {

        class MessageListenerImpl implements MessageListener {
            int peerid;

            public MessageListenerImpl(int peerid) {
                this.peerid = peerid;

            }

            public Object parseMessage(Object obj) {

                // TextIO textIO = TextIoFactory.getTextIO();
                // TextTerminal terminal = textIO.getTextTerminal();
                // terminal.printf("\n"+peerid+"] (Direct Message Received) "+obj+"\n\n");
                System.out.println(obj);
                return "success";

            }

        }

        // final List<Integer> _answersList = new ArrayList<Integer>();
        peer = new SocialImplementation(Integer.parseInt(args[0]), "127.0.0.1",
                new MessageListenerImpl(Integer.parseInt(args[0])));

        System.out.println("<<<-----------WELCOME TO SOCIAL----------->>>");
        // System.out.println("Peer number ----> " + args[0]);
        Functions.getAnswers(peer.getUserProfileQuestions(), peer._user.get_answers());
        Functions.showAnswers(peer._user.get_answers());
        String user = peer.createAuserProfileKey(peer._user.get_answers());
        // System.out.println("Peer ProfileKey ----> " + user);
        System.out.println("Inserisci un nickname");

        String nick = new String();
        int success = 0;
        do {
            nick = input.nextLine();
            if (peer.validateNick(nick)) {
                System.out.println(peer.join(user, nick));
                success = 1;
            } else {
                System.out.println("Nickname alredy exist");
            }
        } while (success != 1);

        int m;
        do {
            System.out.println("<<<-----------Menù----------->>>");
            System.out.println("<<<-----------1 visualizza amici----------->>>");
            System.out.println("<<<-----------2 Search for nickname----------->>>");
            System.out.println("<<<-----------3 Esci----------->>>");
            m = input.nextInt();

            switch (m) {
                case 1:
                    System.out.println("Friends!");
                    list = peer.getFriends();
                    // System.out.println("list ->>" + list);
                    System.out.println("user_list ->" + peer._user.getFriends());
                    break;
                case 2:
                    System.out.println("Search username");
                    System.out.println("Insert nick");
                    String n = input.next();
                    peer.searchUser(n);
                    break;

                case 3:
                    System.out.println("Leave Network");
                    peer.leaveNetwork();
                    break;

            }
        } while (m != 3);
    }

}