package it.unisa.adc.p2p;

import java.util.List;
import java.util.Scanner;

public class Functions {
    static Scanner input = new Scanner(System.in);

    public static void getAnswers(List<String> _questionsList, List<Integer> _answersList) {
        int i, a, n = _questionsList.size();

        for (i = 0; i < n; i++) {

            do {
                System.out.println(_questionsList.get(i));
                a = input.nextInt();
                if (validateInt(a)) {
                    _answersList.add(a);
                } else {
                    System.out.println("INSERT 0 OR 1");
                }

            } while (!validateInt(a));

        }
    }

    public static void showAnswers(List<Integer> _answersList) {
        int i, n = _answersList.size();
        System.out.println("Answer String");
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

    public static boolean weAreFriends(User p1, User p2) {
        int count = 0, a, b, i, n = p1._answers.size();
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
        if (count > 2)
            return true;
        else
            return false;
    }

}
