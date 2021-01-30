package it.unisa.adc.p2p;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import it.unisa.adc.p2p.interfaces.MessageListener;
import it.unisa.adc.p2p.object.User;
import it.unisa.adc.p2p.utility.Functions;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class App {
    protected static SocialImplementation peer;
    static List<String> list = new ArrayList<String>();
    static TextIO textIO = TextIoFactory.getTextIO();

    @Option(name = "-m", aliases = "--masterip", usage = "the master peer ip address", required = true)
    private static String master;

    @Option(name = "-id", aliases = "--identifierpeer", usage = "the unique identifier for this peer", required = true)
    private static int id;

    public static void main(String[] args) throws Exception, NumberFormatException {

        class MessageListenerImpl implements MessageListener {
            int peerid;

            public MessageListenerImpl(int peerid) {
                this.peerid = peerid;

            }

            public Object parseMessage(Object obj) {

                TextIO textIO = TextIoFactory.getTextIO();
                TextTerminal terminal = textIO.getTextTerminal();
                terminal.printf(obj + "\n");
                return "success";

            }

        }
        App app = new App();
        final CmdLineParser parser = new CmdLineParser(app);

        try {
            parser.parseArgument(args);
            TextTerminal terminal = textIO.getTextTerminal();
            peer = new SocialImplementation(id, master, new MessageListenerImpl(id));

            terminal.printf("\nWelcome to Semantic Harmony Social Network\n");
            terminal.printf("Staring peer id: %d on master node: %s\n", id, master);
            terminal.printf("\nAnswer the following questions with 0 (false) or 1 (true)\n");
            Functions.getAnswers(peer.getUserProfileQuestions(), peer._user.get_answers());
            Functions.showAnswers(peer._user.get_answers());
            String user = peer.createAuserProfileKey(peer._user.get_answers());
            terminal.printf("Insert nickname:");
            String nick;
            int success = 0;
            do {
                nick = textIO.newStringInputReader().read();
                if (peer.validateNick(nick)) {
                    if (peer.join(user, nick)) {
                        terminal.printf("YOU JOINED\n");
                        success = 1;
                    }

                } else {
                    terminal.printf("Nickname alredy exist\n");
                }
            } while (success != 1);
            update(terminal);
            int option;
            do {

                showMenu(terminal);
                option = textIO.newIntInputReader().withMaxVal(4).withMinVal(1).read();

                switch (option) {
                    case 1:
                        list = peer.getFriends();
                        if (list.size() != 0) {
                            terminal.printf("FRIEND LIST [Nickname]: \n");
                            Functions.showFriends(peer._user.getFriends());
                        } else {
                            terminal.printf("YOU DON'T HAVE FRIENDS\n");
                        }
                        break;
                    case 2:
                        terminal.printf("SEARCH USER\n");
                        terminal.printf("INSERT A NICKNAME\n");
                        String newusername = textIO.newStringInputReader().read("\n");
                        User newuser = peer.searchUser(newusername);
                        if (newuser != null) {
                            if (newuser.getNickName().equals(peer._user.getNickName())) {
                                printUser(terminal, newuser.printMe());
                            } else {
                                printUser(terminal, newuser.printUser());
                            }

                        } else {
                            terminal.printf("USER NOT FOUND\n");
                        }
                        break;
                    case 3:
                        terminal.printf("UPDATE DATA USER\n");
                        update(terminal);
                        break;
                    case 4:
                        terminal.printf("LEAVE FROM SOCIAL\n");
                        peer.leaveNetwork();
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            } while (option != 4);
        } catch (CmdLineException clEx) {
            System.err.println("ERROR: Unable to parse command-line options: " + clEx);
        }
    }

    private static void showMenu(TextTerminal terminal) {
        terminal.printf("\nType 1 to show your friends\n");
        terminal.printf("Type 2 to search a people with username\n");
        terminal.printf("Type 3 to update your user data\n");
        terminal.printf("Type 4 to leave from social\n");
    }

    private static void printUser(TextTerminal terminal, String userData) {
        terminal.printf("\nUSER DATA");
        terminal.printf("\n" + userData);
        terminal.printf("\n");
    }

    private static void update(TextTerminal terminal) throws ClassNotFoundException, IOException {
        String temp1, temp2;
        int age;
        terminal.printf("Insert your Name\n");
        temp1 = textIO.newStringInputReader().read();
        terminal.printf("Insert your SurName\n");
        temp2 = textIO.newStringInputReader().read();
        terminal.printf("Insert your age\n");
        age = textIO.newIntInputReader().withMaxVal(100).withMinVal(16).read();
        peer.updateData(peer.myKey, temp1, temp2, age);
    }
}