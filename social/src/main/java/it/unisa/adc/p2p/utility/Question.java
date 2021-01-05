package it.unisa.adc.p2p.utility;

import java.util.ArrayList;
import java.util.List;

public class Question {

    public List<String> _questions = new ArrayList<String>();

    public Question() {
        _questions.add("I have a kind word for everyone. ");
        _questions.add("I feel comfortable around people. ");
        _questions.add("I believe in the importance of art. ");
        _questions.add("I make friends easily. ");
        _questions.add("There are many things that I do not like about myself. ");
        // _questions.add("It’s important to me that people are on time. ");
        // _questions.add("I have a vivid imagination. ");
        // _questions.add("I make plans and stick to them. ");
        // _questions.add("I do not like sport. ");
        // _questions.add("Creative ");
        // _questions.add("Nervous ");
        // _questions.add("Pessimistic ");
        // _questions.add("Imaginative ");

    }

    public List<String> getQuestions() {
        return _questions;
    }
}
