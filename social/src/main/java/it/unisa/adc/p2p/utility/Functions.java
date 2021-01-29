package it.unisa.adc.p2p.utility;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import it.unisa.adc.p2p.object.*;

/*
* This class provides a number of useful tools within the project. 
* In particular, all methods that could be logically separated from the main methods have been described in this class.
*/
public class Functions {
    static Scanner input = new Scanner(System.in);
    static TextIO textIO = TextIoFactory.getTextIO();

    /*
     * This method allows you to fill a list of type INTEGER taken as an input
     * parameter, on the basis of a list of type string (which represents a series
     * of questions).
     */
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

    /*
     * This method allows the printing in the console (terminal) of a list of
     * integers (representing the results obtained from a previous entry by the
     * user).
     */
    public static void showAnswers(List<Integer> _answersList) {
        int i, n = _answersList.size();
        System.out.print("Answer String ==> ");
        for (i = 0; i < n; i++) {
            System.out.print(_answersList.get(i));
        }
        System.out.println("\n");
    }

    /*
     * This method allows the printing in the console (terminal) of a list of
     * Strings (representing the nicknames of peers considered friends).
     */
    public static void showFriends(List<String> _friends) {
        int i, n = _friends.size();
        System.out.println("Friends List");
        for (i = 0; i < n; i++) {
            System.out.println(i + 1 + ". " + _friends.get(i));
        }
    }

    /*
     * This method allows the verification of the value of an integer type variable.
     * Accepted values ​​are only 0 and 1.
     */
    public static boolean validateInt(int n) {
        if (n == 0 || n == 1)
            return true;
        else
            return false;
    }

    /* This method allows the presence of a string if it is present in a list. */
    public static boolean checkExistence(List<String> list, String check) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(check)) {
                return false;
            }
        }
        return true;
    }

    /*
     * This method allows you to establish whether two users (type User) within the
     * p2p network can be friends. To determine this, this method takes two users as
     * input, checks the response vector of individual users and calculates their
     * Hamming distance. In the event that the two vectors differ by a maximum of 5
     * out of 10 (value of the calculated Hamming distance), they will be considered
     * friends within the system. No otherwise.
     */
    public static boolean weAreFriends(User p1, User p2) {
        int count = 0, a, b, i, n = p1._answers.size();
        if (!p1.getProfileKey().equals(p2.getProfileKey())) {
            for (i = 0; i < n; i++) {
                a = p1._answers.get(i);
                b = p2._answers.get(i);
                if (a != b) {
                    // System.out.println(a + " == " + b);
                    count++;
                } else {
                    // System.out.println(a + " != " + b);
                }
            }
        }
        if (count <= 5)
            return true;
        else
            return false;
    }

    /*
     * This method provides a string key determined by a simple algorithm on a small
     * alphabet.
     */
    public static String createKey(List<Integer> _answer) {
        final Random rand = new Random();
        int j = 0;
        char[] alphabet = new char[26];
        for (int i = 0; i < 26; i++) {
            alphabet[i] = (char) (97 + i);
        }

        StringBuilder sb = new StringBuilder(20);
        char ch;
        int r;
        while (sb.length() < 20) {
            r = rand.nextInt(26);
            ch = alphabet[r];
            if (sb.length() > 0) {
                if (sb.charAt(sb.length() - 1) != ch) {
                    sb.append(ch);
                    sb.append(_answer.get(j));
                }
            } else {
                sb.append(ch);
                sb.append(_answer.get(j));
            }
            j++;
        }
        return sb.toString();
    }
}
