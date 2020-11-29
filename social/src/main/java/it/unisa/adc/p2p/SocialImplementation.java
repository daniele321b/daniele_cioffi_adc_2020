package it.unisa.adc.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
// import net.tomp2p.peers.PeerAddress;
// import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class SocialImplementation implements SocialInterface {

    // String _profile_key = new String();
    // String _nick_name = new String();

    // public List<String> _friends = new ArrayList<String>();
    // HashSet<PeerAddress> _friends;
    public List<String> _questions = new ArrayList<String>();

    final private PeerDHT _dht;
    final private Peer peer;
    final private int DEFAULT_MASTER_PORT = 4000;

    // public int id;

    String profile_key;

    // final private ArrayList<String> s_topics = new ArrayList<String>();
    User _user = new User();

    public SocialImplementation(int peerId, String masterId) throws Exception {
        peer = new PeerBuilder(Number160.createHash(peerId)).ports(DEFAULT_MASTER_PORT + peerId).start();
        _dht = new PeerBuilderDHT(peer).start();
        _user.setId(peerId);

        FutureBootstrap fb = this.peer.bootstrap().inetAddress(InetAddress.getByName(masterId))
                .ports(DEFAULT_MASTER_PORT).start();
        fb.awaitUninterruptibly();
        if (fb.isSuccess()) {
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    public void store(String name, String nickname) throws IOException {
        _user.setUserName(nickname);
        _dht.put(Number160.createHash(name)).data(new Data(_user)).start().awaitUninterruptibly();
        // System.out.print("STORE OF " + _user.userName);

    }

    public User get(String name) throws ClassNotFoundException, IOException {
        FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            // return futureGet.dataMap().values().iterator().next().object().toString();
            return (User) futureGet.dataMap().values().iterator().next().object();
        }
        return null;
    }

    public String getUsername(String name) throws ClassNotFoundException, IOException {
        FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            // return futureGet.dataMap().values().iterator().next().object().toString();
            User main = (User) futureGet.dataMap().values().iterator().next().object();
            // System.out.print("GET OF " + main.userName);
            return main.userName;
        }
        return null;
    }

    // public void store(String name, String msg) throws IOException {

    // _dht.put(Number160.createHash(name)).data(new
    // Data(msg)).start().awaitUninterruptibly();

    // }

    // public String get(String name) throws ClassNotFoundException, IOException {
    // FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
    // futureGet.awaitUninterruptibly();
    // if (futureGet.isSuccess()) {
    // return futureGet.dataMap().values().iterator().next().object().toString();
    // }
    // return "not found";
    // }

    @Override
    public List<String> getUserProfileQuestions() {
        System.out.println("ANSWER THE FOLLOWING QUESTIONS");
        System.out.println("Answer with code 1 for YES, 0 for NOT");
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

        return _questions;
    }

    @Override
    public String createAuserProfileKey(List<Integer> _answers) {
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwyxz";
        int i, n = _answers.size();
        String profileKey = new String();
        for (i = 0; i < n; i++) {
            profileKey = profileKey + _answers.get(i) + alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return profileKey;

    }

    @Override
    public boolean join(String _profile_key, String _nick_name) {

        if (_profile_key != null) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<String> getFriends() {
        return _user.friends;
    }

}
