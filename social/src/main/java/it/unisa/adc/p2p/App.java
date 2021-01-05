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

    // come prova

    public static void main(String[] args) throws Exception, NumberFormatException {
        final List<Integer> _answersList = new ArrayList<Integer>();
        peer = new SocialImplementation(Integer.parseInt(args[0]), "127.0.0.1", null);
        System.out.println("<<<-----------WELCOME TO SOCIAL----------->>>");
        System.out.println("Peer number ----> " + args[0]);
        Functions.getAnswers(peer.getUserProfileQuestions(), _answersList);
        Functions.showAnswers(_answersList);

        
    }

}