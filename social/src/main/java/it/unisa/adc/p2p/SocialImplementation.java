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
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class SocialImplementation implements SocialInterface {
    final private Peer peer;
    String _profile_key = new String();
    String _nick_name = new String();

    public List<String> _friends = new ArrayList<String>();
    public List<String> _questions = new ArrayList<String>();
    public List<Integer> _answers = new ArrayList<Integer>();

    // final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT = 4000;

    int id;
    String peer1;
    String profile_key;

    // final private ArrayList<String> s_topics = new ArrayList<String>();
    final private User user = new User();

    public SocialImplementation(int peerId, String masterId) throws Exception {
        peer = new PeerBuilder(Number160.createHash(peerId)).ports(DEFAULT_MASTER_PORT + peerId).start();
        _dht = new PeerBuilderDHT(peer).start();

        FutureBootstrap fb = this.peer.bootstrap().inetAddress(InetAddress.getByName(masterId))
                .ports(DEFAULT_MASTER_PORT).start();
        fb.awaitUninterruptibly();
        if (fb.isSuccess()) {
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    // public String get(String name) throws ClassNotFoundException, IOException {
    // FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
    // futureGet.awaitUninterruptibly();
    // if (futureGet.isSuccess()) {
    // return futureGet.dataMap().values().iterator().next().object().toString();
    // }
    // return "not found";
    // }
    public String get(String name) throws ClassNotFoundException, IOException {
        FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            return futureGet.dataMap().values().iterator().next().object().toString();
            // return (User) futureGet.dataMap().values().iterator().next().object();
        }
        return "not found";
    }

    public void store(String name, String msg) throws IOException {

        _dht.put(Number160.createHash(name)).data(new Data(msg)).start().awaitUninterruptibly();
        // _dht.put(Number160.createHash(name)).data(new Data((Object)
        // user)).start().awaitUninterruptibly();
    }

    @Override
    public List<String> getUserProfileQuestions() {
        _questions.add("I have a kind word for everyone. ");
        _questions.add("I feel comfortable around people. ");
        _questions.add("I believe in the importance of art. ");
        // _questions.add("I make friends easily. ");
        // _questions.add("There are many things that I do not like about myself. ");
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
    public String createAuserProfileKey(List<Integer> _answer) {

        // this._answers = _answer;
        // this.profile_key = _profile_key;
        // return _profile_key;

        try {
            FutureGet futureGet = _dht.get(Number160.createHash(peer1)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty())
                _dht.put(Number160.createHash(peer1)).data(new Data(new HashSet<PeerAddress>())).start()
                        .awaitUninterruptibly();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

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
        // Object _obj;
        // try {
        // FutureGet futureGet = _dht.get(Number160.createHash(_profile_key)).start();
        // futureGet.awaitUninterruptibly();
        // if (futureGet.isSuccess()) {
        // HashSet<PeerAddress> peers_on_topic;
        // peers_on_topic = (HashSet<PeerAddress>)
        // futureGet.dataMap().values().iterator().next().object();
        // // for (PeerAddress peer : peers_on_topic) {
        // // FutureDirect futureDirect =
        // // _dht.peer().sendDirect(peer).object(_obj).start();
        // // // futureDirect.awaitUninterruptibly();

        // // _friends.add(futureDirect.toString()); }

        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        return _friends;
    }

}
