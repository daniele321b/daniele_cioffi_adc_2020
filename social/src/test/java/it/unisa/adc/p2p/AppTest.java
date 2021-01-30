package it.unisa.adc.p2p;

import org.junit.jupiter.api.*;

import it.unisa.adc.p2p.interfaces.MessageListener;
import it.unisa.adc.p2p.object.User;
import it.unisa.adc.p2p.utility.Functions;

import net.tomp2p.peers.PeerAddress;
import it.unisa.adc.p2p.SocialImplementation;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    protected static SocialImplementation peer0, peer1, peer2, peer3;
    List<Integer> _answers = new ArrayList<>();
    List<String> _questions = new ArrayList<>();
    HashMap<PeerAddress, String> hashMap = new HashMap<>();

    public AppTest() throws Exception {

    }

    static class MessageListenerImpl implements MessageListener {

        int peerid;

        public MessageListenerImpl(int peerid) {
            this.peerid = peerid;
        }

        public Object parseMessage(Object obj) {
            return "success";
        }

    }

    @BeforeAll
    static void initPeers() throws Exception {
        /* Initialization of peers used for testing */
        peer0 = new SocialImplementation(0, "127.0.0.1", new MessageListenerImpl(0));
        peer1 = new SocialImplementation(1, "127.0.0.1", new MessageListenerImpl(1));
        peer2 = new SocialImplementation(2, "127.0.0.1", new MessageListenerImpl(2));
        peer3 = new SocialImplementation(3, "127.0.0.1", new MessageListenerImpl(3));

    }

    @Test
    void testCasePopulateAnswersList() {
        _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        assertTrue(_answers.size() == 10);
    }

    /*
     * Test case to simulate the insertion by the user of an answer or a value equal
     * to 0 (false) or 1 (true).
     */
    @Test
    void testCaseAddAnAnswers() {
        _answers.add(1);
        assertTrue(_answers.size() == 1);
    }

    /*
     * Test case of the validateInt() function that checks if the integer passed as
     * parameter is 0 or 1. In this case it returns true, false otherwise.
     */
    @Test
    void testCaseValidateInt() {
        assertTrue(Functions.validateInt(0));
        assertTrue(Functions.validateInt(1));
        assertFalse(Functions.validateInt(4));
    }

    /*
     * Test case of the checkExitense() function that verifies if the string passed
     * as a parameter is present in the string list always supplied as a parameter.
     * The function returns true if not present, false otherwise.
     */
    @Test
    void testCaseCheckExistence() {
        List<String> list = new ArrayList<>(Arrays.asList("string1", "string2", "string3"));
        assertFalse(Functions.checkExistence(list, "string2"));
        assertTrue(Functions.checkExistence(list, "string4"));
    }

    /*
     * Test case of the weAreFriends function that checks if two users (User class)
     * passed as a parameter are friends. The function calculates the Hamming
     * distance between the "answers" vectors of both users. The function returns
     * true if the Hamming distance is less than or equal to 5, false otherwise.
     */
    @Test
    void testCaseWeAreFriends() {
        User user1 = new User();
        User user2 = new User();
        List<Integer> _answers1 = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0));
        List<Integer> _answers2 = new ArrayList<>(Arrays.asList(0, 1, 0, 1, 1, 0, 0, 0, 1, 1));

        user1.setProfileKey("profileKey1");
        user2.setProfileKey("profileKey2");
        user1.set_answers(_answers1);
        user2.set_answers(_answers1);
        assertTrue(Functions.weAreFriends(user1, user2));
        user1.set_answers(_answers1);
        user2.set_answers(_answers2);
        assertFalse(Functions.weAreFriends(user1, user2));
    }

    /*
     * Test case of the createKey function that creates a string (key) using the
     * response vector taken as a parameter. The test will return true if the string
     * it returns is not null and if it has length 10.
     */
    @Test
    void testCaseCreateKey() {
        List<Integer> _answers = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0));
        assertTrue(Functions.createKey(_answers).length() == 20);
        assertTrue(!Functions.createKey(_answers).equals(null));
    }

    /*
     * Test cases of the main functions that are used for the correct functioning of
     * the p2p network...
     */

    /*
     * Test case to simulate entry creation in DHT to save a HashMap to be used for
     * storing PeerAddress-String pairs (Peer key)
     */
    @Test
    void testCaseCreatePeerAddressList() {
        assertTrue(peer0.creatPeerAddressList());
    }

    /* Test case to simulate returning a single question (string) to the user. */
    @Test
    void testCaseGetUserProfileQuestionsSingleQuestion() {
        assertTrue(peer0.getUserProfileQuestions().get(0) != null);
    }

    /*
     * Test case to simulate returning all questions (string) to the user. To test
     * this, it is checked whether its magnitude is equal to 10.
     */
    @Test
    void testCaseGetUserProfileQuestions() {
        assertTrue(peer0.getUserProfileQuestions().size() == 10);
    }

    /*
     * Test case to simulate the creation of a profile key for a user. This function
     * takes advantage of the result of another function previously tested. Returns
     * false if it is null.
     */
    @Test
    void testCreateAuserProfileKey() {
        List<Integer> _answers = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0));
        assertFalse(peer1.createAuserProfileKey(_answers) == null);
    }

    /*
     * Test case of the send Notification () function. This function is called by
     * the Join () function but its result (true or false) does not affect the
     * completion of it. Even in the case of an empty hashMap (if there are no peers
     * inside the network) it does not fail but does not send any message. To test
     * the case where messages are sent you need to test the Join function where it
     * is included.
     */
    @Test
    void testCaseSendNotification() {
        _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        peer0.getUserProfileQuestions();
        peer0._user.set_answers(_answers);
        peer0.createAuserProfileKey(peer0._user.get_answers());
        assertTrue(peer0.sendNotification(hashMap, "Message"));
    }

    /*
     * Test case of the join () function. This function allows access to the user's
     * social network. It needs a profile key (previously generated by another
     * function) and a nickname entered by the user. Returns true if the login was
     * successful and false otherwise.
     */
    @Test
    void testCaseJoin() {
        List<Integer> _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        peer0.getUserProfileQuestions();
        peer0._user.set_answers(_answers);
        String user = peer0.createAuserProfileKey(peer0._user.get_answers());
        peer0.validateNick("pippo");
        assertTrue(peer0.join(user, "pippo"));
    }

    /*
     * Test case of the updateData () function. This test first predicts that a peer
     * has logged into the network and then subsequently wants to change their
     * personal data. It returns true if the operation is successful, false
     * otherwise. This method involves throwing an exception in case of failure.
     */
    @Test
    void testCaseUpdateData() throws ClassNotFoundException, IOException {
        _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        peer0.getUserProfileQuestions();
        peer0._user.set_answers(_answers);
        String user = peer0.createAuserProfileKey(peer0._user.get_answers());
        peer0.validateNick("pippo");
        peer0.join(user, "pippo");
        assertTrue(peer0.updateData(user, "Caio", "Tizio", 25));
    }

    /*
     * Test case of the get Peers Object function. This test simulates the call to
     * the getPeersObject function which returns as many User objects as those
     * present in the network.
     */
    @Test
    void testCaseGetPeersObject() {
        List<Integer> _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        List<Integer> _answers2 = Arrays.asList(1, 1, 1, 1, 0, 0, 0, 1, 1, 0);

        peer0.getUserProfileQuestions();
        peer0._user.set_answers(_answers);
        String user = peer0.createAuserProfileKey(_answers);
        peer0.validateNick("pippo");
        peer0.join(user, "pippo");

        peer1.getUserProfileQuestions();
        peer1._user.set_answers(_answers2);
        String user1 = peer1.createAuserProfileKey(_answers2);
        peer1.validateNick("pluto");
        peer1.join(user1, "pluto");

        List<User> list1 = peer0.getPeersObject();
        // System.out.println(list1);
        assertTrue(list1.size() == 2);
    }

    /*
     * Test case of the get Friends function in case it returns an empty list. This
     * test first simulates the access to the network of two peers having a
     * different response vector and then the request to verify who their friends
     * are in the network. In this case the test returns true if the friends list is
     * empty and has size 0.
     */

    @Test
    void testCaseGetFriendsEmpty() {
        // different response vectors
        List<Integer> _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        List<Integer> _answers2 = Arrays.asList(1, 1, 1, 1, 0, 0, 0, 1, 1, 0);

        peer0.getUserProfileQuestions();
        peer0._user.set_answers(_answers);
        String user = peer0.createAuserProfileKey(_answers);
        peer0.validateNick("pippo");
        peer0.join(user, "pippo");

        peer1.getUserProfileQuestions();
        peer1._user.set_answers(_answers2);
        String user1 = peer1.createAuserProfileKey(_answers2);
        peer1.validateNick("pluto");
        peer1.join(user1, "pluto");

        List<String> list1 = new ArrayList<String>();
        list1 = peer0.getFriends();
        assertTrue(list1.size() == 0);
        assertTrue(list1.isEmpty());

    }

    /*
     * Test case of the get Friends function if it returns a list that is not empty.
     * This test first simulates the access to the network of two peers having the
     * same response vector and then the request to verify who their friends are in
     * the network. In this case the test returns true if the friends list contains
     * a nickname, 0 otherwise.
     */
    @Test
    void testCaseGetFriendsNotEmpty() {
        // same vector answers
        List<Integer> _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        peer0.getUserProfileQuestions();
        String user = peer0.createAuserProfileKey(_answers);
        peer0.validateNick("pippo");
        peer0.join(user, "pippo");

        peer1.getUserProfileQuestions();
        String user1 = peer1.createAuserProfileKey(_answers);
        peer1.validateNick("pluto");
        peer1.join(user1, "pluto");

        List<String> list1 = new ArrayList<>();
        list1 = peer1.getFriends();
        // System.out.println(list1);
        assertTrue(list1.size() == 1);
    }

    /*
     * Test case of the get function. This test simulates the call to the function
     * getun User object having as key the string passed as parameter. The test
     * returns true if the object is found, false otherwise. This method involves
     * throwing an exception in case of failure.
     */

    @Test
    void testCaseGet() throws ClassNotFoundException, IOException {
        List<Integer> _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        peer0.getUserProfileQuestions();
        String user = peer0.createAuserProfileKey(_answers);
        peer0.validateNick("pippo");
        peer0.join(user, "pippo");

        peer1.getUserProfileQuestions();
        String user1 = peer1.createAuserProfileKey(_answers);
        peer1.validateNick("pluto");
        peer1.join(user1, "pluto");
        User new_user = peer0.get(user1);
        assertTrue(!new_user.equals(null));
    }

    /*
     * Test case of the leaveNetwork () function. In this case, first the access of
     * a peer to the network is simulated and then its exit.
     */
    @Test
    void testCaseLeaveNetwork() {
        List<Integer> _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        peer0.getUserProfileQuestions();
        peer0._user.set_answers(_answers);
        String user = peer0.createAuserProfileKey(peer0._user.get_answers());
        peer0.validateNick("pippo");
        peer0.join(user, "pippo");
        assertTrue(peer0.leaveNetwork());
    }

    /*
     * Test case of the searchUser () function. Two peers are logged into the
     * network and one of them searches for the other by entering only the nickname.
     * The test returns true if the user is found and false otherwise.
     */
    @Test
    void testCaseSearchUser() {

        List<Integer> _answers = Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 0);
        peer0.getUserProfileQuestions();
        String user = peer0.createAuserProfileKey(_answers);
        peer0.validateNick("pippo");
        peer0.join(user, "pippo");

        peer1.getUserProfileQuestions();
        String user1 = peer1.createAuserProfileKey(_answers);
        peer1.validateNick("pluto");
        peer1.join(user1, "pluto");

        User new_user = peer0.searchUser("pluto");
        // System.out.println(new_user);
        assertTrue(new_user.getNickName() != null);

    }

}