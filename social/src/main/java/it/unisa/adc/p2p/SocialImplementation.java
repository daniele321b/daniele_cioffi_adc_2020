package it.unisa.adc.p2p;

import it.unisa.adc.p2p.interfaces.*;
import it.unisa.adc.p2p.object.User;
import it.unisa.adc.p2p.utility.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class SocialImplementation implements SocialInterface {

    final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT = 4000;
    String myKey;
    private static int peerId;
    private HashSet<String> myFriends = new HashSet<>();
    User _user = new User();

    public SocialImplementation(int _id, String _master_peer, final MessageListener _listener) throws Exception {
        peerId = _id;
        peer = new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_MASTER_PORT + _id).start();
        _dht = new PeerBuilderDHT(peer).start();

        FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer))
                .ports(DEFAULT_MASTER_PORT).start();
        fb.awaitUninterruptibly();
        if (fb.isSuccess()) {
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        } else {
            throw new Exception("Error in master peer bootstrap.");
        }

        peer.objectDataReply(new ObjectDataReply() {
            public Object reply(PeerAddress sender, Object request) throws Exception {
                return _listener.parseMessage(request);
            }
        });
    }

    public List<String> getUserProfileQuestions() {
        Question questions = new Question();
        return questions.getQuestions();
    }

    public String createAuserProfileKey(List<Integer> _answer) {
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
        this.myKey = sb.toString();
        return sb.toString();
        // return null;
    }

    public boolean join(String _profile_key, String _nick_name) {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_profile_key)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty()) {
                // _dht.put(Number160.createHash(_profile_key)).data(new Data(new
                // HashSet<PeerAddress>())).start()
                // .awaitUninterruptibly();
                _user.setProfileKey(_profile_key);
                _user.setNickName(_nick_name);
                _dht.put(Number160.createHash(_profile_key)).data(new Data(_user)).start().awaitUninterruptibly();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public List<String> getFriends() {
        List<String> list = new ArrayList<String>();
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_user.getProfileKey())).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                HashSet<PeerAddress> peers_on_topic;
                peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
                for (PeerAddress peer : peers_on_topic) {
                    FutureDirect futureDirect = _dht.peer().sendDirect(peer).object("_obj").start();
                    futureDirect.awaitUninterruptibly();
                }

                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

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

}