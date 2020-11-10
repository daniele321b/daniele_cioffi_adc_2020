package it.unisa.adc.p2p;

import java.net.InetAddress;
import java.util.*;

import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class SocialImplementation implements SocialInterface {
    // final private Peer peer;
    String _profile_key = new String();
    public List<Integer> _answers = new ArrayList<Integer>();
    public List<String> _friends = new ArrayList<String>();
    public List<String> _questions = new ArrayList<String>();

    final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT = 4000;

    int id;
    String peer1;
    String profile_key;

    public SocialImplementation(int _id, String _master_peer, final MessageListener _listener) throws Exception {
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

    @Override
    public List<String> getUserProfileQuestions() {
        _questions.add("Question NUMBER 1");
        _questions.add("Question NUMBER 2");
        _questions.add("Question NUMBER 3");
        _questions.add("Question NUMBER 4");
        _questions.add("Question NUMBER 5");

        return _questions;
    }

    @Override
    public String createAuserProfileKey(List<Integer> _answer) {

        this._answers = _answer;
        this.profile_key = _profile_key;
        return _profile_key;
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
