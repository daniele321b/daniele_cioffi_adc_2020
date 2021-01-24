package it.unisa.adc.p2p.utility;

import java.util.ArrayList;
import java.util.List;

/* 
* This class describes a list of questions. Unfortunately, these questions can only be added manually statically. 
* The questions are used during the access procedures to the p2p network. 
* The class has a single method that allows you to return a list of strings (questions).
*/
public class Question {

    public List<String> _questions = new ArrayList<String>();

    public Question() {
        _questions.add("1 - I have a kind word for everyone. ");
        _questions.add("2 - I feel comfortable around people. ");
        _questions.add("3 - I believe in the importance of art. ");
        _questions.add("4 - I make friends easily. ");
        _questions.add("5 - There are many things that I do not like about myself. ");
        _questions.add("6 - It’s important to me that people are on time. ");
        _questions.add("7 - I have a vivid imagination. ");
        _questions.add("8 - I make plans and stick to them. ");
        _questions.add("9 - I do not like sport. ");
        _questions.add("10 - Creative ");
        // _questions.add("11 - Nervous ");
        // _questions.add("12 - Pessimistic ");
        // _questions.add("13 - Imaginative ");

    }

    // This method returns a list of strings (questions).
    public List<String> getQuestions() {
        return _questions;
    }
}
