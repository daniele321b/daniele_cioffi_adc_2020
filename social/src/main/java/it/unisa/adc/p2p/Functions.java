package it.unisa.adc.p2p;

import java.util.List;
import java.util.Scanner;

public class Functions {
    static Scanner input = new Scanner(System.in);

    public static void getAnswers(List<String> _questionsList, List<Integer> _answersList) {
        int i, a, n = _questionsList.size();
        System.out.println("Answer the following questions(with code 1 for Yes, 0 for NOT)");
        for (i = 0; i < n; i++) {
            System.out.println(_questionsList.get(i));
            a = input.nextInt();
            if (validateInt(a))
                _answersList.add(a);
        }
    }

    public static boolean validateInt(int n) {
        if (n == 0 || n == 1)
            return true;
        else
            return false;
    }

    public static boolean weAreFriends(SocialImplementation p1, SocialImplementation p2) {
        int count = 0, i, n = p1._answers.size();
        for (i = 0; i < n; i++) {
            if (p1._answers.get(i) == p2._answers.get(i))
                count++;
        }
        if (count > 2)
            return true;
        else
            return false;
    }
}
