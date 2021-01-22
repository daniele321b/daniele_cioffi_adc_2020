package it.unisa.adc.p2p;

import it.unisa.adc.p2p.interfaces.*;
import it.unisa.adc.p2p.object.User;
import it.unisa.adc.p2p.utility.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FutureSend;
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
        creatPeerAddressList();

        peer.objectDataReply(new ObjectDataReply() {
            public Object reply(PeerAddress sender, Object request) throws Exception {
                return _listener.parseMessage(request);
            }
        });
    }

    public void creatPeerAddressList() {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash("peerAddress")).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty())
                _dht.put(Number160.createHash("peerAddress")).data(new Data(new HashMap<PeerAddress, String>())).start()
                        .awaitUninterruptibly();
            // System.out.println("Future get create list ---> " + futureGet.isSuccess());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<String> getUserProfileQuestions() {
        Question questions = new Question();
        return questions.getQuestions();
    }

    public String createAuserProfileKey(List<Integer> _answer) {
        myKey = Functions.createKey(_answer);
        return myKey;
    }

    public boolean sendNotification(HashMap<PeerAddress, String> hashMap, Object _obj) {
        try {

            for (PeerAddress peer : hashMap.keySet()) {
                FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(_obj).start();
                futureDirect.awaitUninterruptibly();
            }
            // System.out.println("TRUE SEND NOTIFICATION");
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean join(String _profile_key, String _nick_name) {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_profile_key)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty()) {
                _user.setProfileKey(_profile_key);
                _user.setNickName(_nick_name);
                System.out
                        .println("Dati che sto inserendo ----> " + _user.getProfileKey() + " e " + _user.getNickName());
                // entro nella rete inserendo una risorsa (Mio oggetto)
                _dht.put(Number160.createHash(_profile_key)).data(new Data(_user)).start().awaitUninterruptibly();
            }
            FutureGet fuGet = _dht.get(Number160.createHash("peerAddress")).start();
            fuGet.awaitUninterruptibly();
            if (fuGet.isSuccess()) {
                if (fuGet.isEmpty())
                    return false;
                HashMap<PeerAddress, String> peers_on_social;
                peers_on_social = (HashMap<PeerAddress, String>) fuGet.dataMap().values().iterator().next().object();
                // // mi aggiungo alla lista
                peers_on_social.put(_dht.peerAddress(), _user.getProfileKey());
                _dht.put(Number160.createHash("peerAddress")).data(new Data(peers_on_social)).start()
                        .awaitUninterruptibly();
                sendNotification(peers_on_social, "Likely Friend");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getPeersObject() {
        // System.out.println("get friends");
        List<User> list = new ArrayList<User>();
        User u;
        try {
            FutureGet futureGet = _dht.get(Number160.createHash("peerAddress")).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty())
                    return null;
                HashMap<PeerAddress, String> peers_on_social;
                peers_on_social = (HashMap<PeerAddress, String>) futureGet.dataMap().values().iterator().next()
                        .object();
                // System.out.println("is empty: -> " + peers_on_social.isEmpty() + "size: " +
                // peers_on_social.size());
                // System.out.println("Peer on social value ---> " + peers_on_social.values());
                for (String peer : peers_on_social.values()) {
                    // System.out.println("peer string --> " + peer);
                    u = get(peer);
                    // System.out.println("user---->" + u.getNickName());
                    list.add(u);
                }
                // System.out.println("Peers on social " + peers_on_social);
            }
            // System.out.println("Future get peers object ---> " + futureGet.isSuccess());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getFriends() {

        List<User> list_users = getPeersObject();
        List<String> list = new ArrayList<String>();
        for (User u : list_users) {
            if (Functions.weAreFriends(_user, u)) {
                list.add(u.getNickName());
            }
        }
        _user.setFriends(list);
        return list;
    }

    public User get(String name) throws ClassNotFoundException, IOException {
        FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            return (User) futureGet.dataMap().values().iterator().next().object();
        }
        return null;
    }

    public boolean leaveNetwork() {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash("peerAddress")).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty())
                    return false;
                HashMap<PeerAddress, String> peers_on_social;
                peers_on_social = (HashMap<PeerAddress, String>) futureGet.dataMap().values().iterator().next()
                        .object();
                peers_on_social.remove(_dht.peerAddress());
                _dht.put(Number160.createHash("peerAddress")).data(new Data(peers_on_social)).start()
                        .awaitUninterruptibly();
                _dht.peer().announceShutdown().start().awaitUninterruptibly();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void searchUser(String nickname) {
        String a, b;
        List<User> list_users = getPeersObject();
        for (User u : list_users) {
            a = u.getNickName();
            if (a.toUpperCase().equals(nickname.toUpperCase())) {
                u.printUser();
                return;
            }
        }
    }
}