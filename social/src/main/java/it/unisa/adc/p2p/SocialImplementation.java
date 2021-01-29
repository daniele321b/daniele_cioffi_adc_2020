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
/* 
* This class represents the core of the project. 
* Firstly because it implements the methods necessary to respond to the interface provided by the teacher, 
* and secondly because it implements all the methods necessary for the functioning of the p2p network. 
* In particular, the methods described are used to: 
* 1 - create a peer; 
* 2 - initialize and then fill a list with all peers in the system; 
* 3 - return a series of questions to be provided to the user; 
* 4 - send a message using the p2p network; join in the social network; 
* 5 - modify the user's personal data available on the network; 
* 6 - return a User type list; 
* 7 - return the list of friends for each peer; 
* 8 - return a single User object present in the p2p network; 
* 9 - search for a user in the p2p network; 
* 10 - leave the p2p network.
*/

public class SocialImplementation implements SocialInterface {

    final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT = 4000;
    String myKey;
    private static int peerId;
    User _user = new User();

    /*
     * This constructor allows the creation of an object of type
     * SocialImplmentation. It needs three input parameters: peer id, reference to
     * the (known) masterpeer, and an object of type MessageListener. By exploiting
     * the knowledge of the master peer, the peer succeeds in calling the
     * bootstrap() method, necessary for the first access to a p2p network.
     */
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

    /*
     * This method allows the creation of a HashMap <PeerAddress, String> type data
     * inserted in the network that will be used to take note of all the peers
     * present in this system. This solution is certainly not very scalable for a
     * large number of users, but it has been taken into account that the creation
     * of this p2p network is for educational purposes. Therefore this solution is
     * not that bad.
     */
    public boolean creatPeerAddressList() {// changed from void to boolean
        try {
            FutureGet futureGet = _dht.get(Number160.createHash("peerAddress")).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty()) {
                _dht.put(Number160.createHash("peerAddress")).data(new Data(new HashMap<PeerAddress, String>())).start()
                        .awaitUninterruptibly();

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /*
     * Method required by the interface. This method returns a list of questions
     * (Strings) that will be provided to the user using an auxiliary method
     * described in another class.
     */
    public List<String> getUserProfileQuestions() {
        Question questions = new Question();
        return questions.getQuestions();
    }

    /*
     * Method required by the interface. This method allows the creation of a string
     * type user profile key. Again you need the help of another createKey ()
     * function defined in another class.
     */
    public String createAuserProfileKey(List<Integer> _answer) {
        int success = 0;
        do {
            myKey = Functions.createKey(_answer);
            if (validateKey(myKey))
                success = 1;
        } while (success != 1);
        return myKey;
    }

    /*
     * This method is used to send an Object type message to peers on the system. In
     * particular, taking a HashMap of addresses and an object in input, using the
     * senDirect() method it is possible to send the object in question to a peer.
     * Inside the method there is a loop to send this object to all peers already
     * present in the p2p network.
     */
    public boolean sendNotification(HashMap<PeerAddress, String> hashMap, Object _obj) {
        try {

            for (PeerAddress peer : hashMap.keySet()) {
                if (_dht.peerAddress() != peer) {
                    FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(_obj).start();
                    futureDirect.awaitUninterruptibly();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Method required by the interface. The join method allows the peer created
     * previously to access the social network. To carry out this operation, the
     * method requires two fundamental parameters: a fundamental profile key to
     * identify the User object to be inserted in the p2p network as a resource
     * (String); and a nick name useful for identifying a user within a set of users
     * in a user-friendly way. In this method there are two distinct phases: the
     * first consists in the insertion of the User object of the peer inside the p2p
     * network using the functions get(), put(), etc ... of the library; the second
     * consists in sending a message to all the peers present in the system to
     * notify the access to it. This process is offered by the sendNotification()
     * function described above.
     */

    public boolean join(String _profile_key, String _nick_name) {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_profile_key)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty()) {
                // Setting the basic information of the peer
                _user.setProfileKey(_profile_key);
                _user.setNickName(_nick_name);
                _dht.put(Number160.createHash(_profile_key)).data(new Data(_user)).start().awaitUninterruptibly();
            }
            FutureGet fuGet = _dht.get(Number160.createHash("peerAddress")).start();
            fuGet.awaitUninterruptibly();
            if (fuGet.isSuccess()) {
                if (fuGet.isEmpty())
                    return false;
                // I find the list of peers present in the network
                HashMap<PeerAddress, String> peers_on_social;
                peers_on_social = (HashMap<PeerAddress, String>) fuGet.dataMap().values().iterator().next().object();
                // Adding my address to the list
                peers_on_social.put(_dht.peerAddress(), _user.getProfileKey());
                _dht.put(Number160.createHash("peerAddress")).data(new Data(peers_on_social)).start()
                        .awaitUninterruptibly();
                // Notification to all peers of access to the social network
                sendNotification(peers_on_social, _nick_name + " and YOU are probably Friend");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * This method allows the modification of the attributes of the peer object
     * within the p2p network. Note: this modification is made on an object already
     * inserted in the network. Therefore, it is first retrieved from it, modified
     * through the set () methods of the object itself and subsequently re-inserted
     * into the system.
     */
    public boolean updateData(String _profile_key, String _name, String _surname, int _age)
            throws ClassNotFoundException, IOException {
        User user = get(_profile_key);
        user.setAge(_age);
        user.setName(_name);
        user.setSurname(_surname);
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_profile_key)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                _dht.put(Number160.createHash(_profile_key)).data(new Data(user)).start().awaitUninterruptibly();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * This method allows you to find a list of User type objects of all peers
     * present in the p2p system. In particular, this method, exploiting the
     * knowledge of the peerAddress entry within the DHT and the associated hashmap,
     * it is able to retrieve every single user object within the network. This
     * operation is guaranteed by the get function (which takes a string as input,
     * in our case the profilekey).
     */
    public List<User> getPeersObject() {
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
                for (String peer : peers_on_social.values()) {
                    // Found the User object
                    u = get(peer);
                    // Add the User object found to a list of users
                    list.add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* Method to check if the generated key is already present. */
    public boolean validateKey(String key) {
        List<User> list_users = getPeersObject();
        // if list_user !=null
        for (User u : list_users) {
            if (u.getProfileKey().equals(key)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Method to check if the entered nickname has already been chosen by another
     * user.
     */
    public boolean validateNick(String nick) {
        List<User> list_users = getPeersObject();
        for (User u : list_users) {
            if (u.getNickName().equals(nick)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Method required by the interface. This method returns a user's friends list.
     * This list is determined on the basis of the response vectors that each user
     * has generated by answering a series of questions when accessing the network.
     * The weAreFriends function determines following the calculation of the hamming
     * distance if the two users are friends. If so, the user's nickname is added to
     * the friends list.
     */
    public List<String> getFriends() {

        List<User> list_users = getPeersObject();
        List<String> list = new ArrayList<String>();
        if (!list_users.isEmpty()) {
            for (User u : list_users) {
                if (!_user.getNickName().equals(u.getNickName())) {
                    if (Functions.weAreFriends(_user, u)) {
                        list.add(u.getNickName());
                    }
                }
            }
        }
        _user.setFriends(list);
        return list;
    }

    /*
     * Simple method that allows you to return a user object within the p2p network
     * by providing its profile key.
     */
    public User get(String name) throws ClassNotFoundException, IOException {
        FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            return (User) futureGet.dataMap().values().iterator().next().object();
        }
        return null;
    }

    /*
     * This method allows the peer to leave the system. In particular, before
     * announcing to the other peers its exit, the peer undertakes to eliminate
     * itself from the list of peers present in the network.
     */
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

    /*
     * Method that allows you to search for a user within the system by providing
     * only his nickname.
     */
    public User searchUser(String nickname) {
        String a;
        List<User> list_users = getPeersObject();
        for (User u : list_users) {
            a = u.getNickName();
            if (a.toUpperCase().equals(nickname.toUpperCase())) {
                return u;
            }
        }
        return null;
    }
}