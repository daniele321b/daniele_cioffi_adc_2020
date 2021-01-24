package it.unisa.adc.p2p.utility;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import it.unisa.adc.p2p.object.*;

public class Functions {
    static Scanner input = new Scanner(System.in);
    static TextIO textIO = TextIoFactory.getTextIO();

    public static void getAnswers(List<String> _questionsList, List<Integer> _answersList) {
        int i, a, n = _questionsList.size();
        for (i = 0; i < n; i++) {
            do {
                System.out.println(_questionsList.get(i));
                a = textIO.newIntInputReader().withMaxVal(1).withMinVal(0).read();
                _answersList.add(a);
            } while (!validateInt(a));
        }
    }

    public static void showAnswers(List<Integer> _answersList) {
        int i, n = _answersList.size();
        System.out.print("Answer String ==> ");
        for (i = 0; i < n; i++) {
            System.out.print(_answersList.get(i));
        }
        System.out.println("\n");
    }

    public static void showFriends(List<String> _friends) {
        int i, n = _friends.size();
        System.out.println("Friends List");
        for (i = 0; i < n; i++) {
            System.out.println(i + 1 + ". " + _friends.get(i));
        }
    }

    public static boolean validateInt(int n) {
        if (n == 0 || n == 1)
            return true;
        else
            return false;
    }

    public static boolean checkExistence(List<String> list, String check) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(check)) {
                return false;
            }
        }
        return true;
    }

    public static boolean weAreFriends(User p1, User p2) {
        int count = 0, a, b, i, n = p1._answers.size();
        if (!p1.getProfileKey().equals(p2.getProfileKey())) {
            for (i = 0; i < n; i++) {
                a = p1._answers.get(i);
                b = p2._answers.get(i);
                if (a == b) {
                    // System.out.println(a + " == " + b);
                    count++;
                } else {
                    // System.out.println(a + " != " + b);
                }
            }
        }
        if (count >= 5)
            return true;
        else
            return false;
    }

    public static String createKey(List<Integer> _answer) {
        final Random rand = new Random();
        char[] alphabet = new char[26];
        for (int i = 0; i < 26; i++) {
            alphabet[i] = (char) (97 + i);
        }
        StringBuilder sb = new StringBuilder(10);
        while (sb.length() < 10) {
            char ch = alphabet[rand.nextInt(26)];
            if (sb.length() > 0) {
                if (sb.charAt(sb.length() - 1) != ch) {
                    sb.append(ch);
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
