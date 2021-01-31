# Semantic Harmony Social Network

<img src="https://s3.amazonaws.com/lowres.cartoonstock.com/media-social_media-interests-compatibility-dates-social_networks-jsh120327_low.jpg" style="zoom:30%">

**Candidate:** Cioffi Daniele - 0522500753

**Academic Year:** 2020/2021

**Distributed architectures for the cloud**



# Summary.

1. Presentation of the solution

2. Technologies used

3. Structure of the project

   3.1 Description of the code

4. Test Cases

5. Dependencies

6. Docker

7. Conclusion and future developments



# 1. Presentation of the solution

Semantic Harmony Social Network is a peer-to-peer (p2p) social network based on user interests.

The main goal of the project is to provide a p2p system that allows users to automatically establish friendships, all through a matching strategy.

APIs have been provided that allow each user who accesses the network to:

1. Receive a series of questions that will allow matching with other users (friendship).
2. Create a profile key that will identify the user within the social network.
3. Log in to the social network.
4. Display the list of friends (automatically established).

In addition to these features, three more have also been implemented:

1. Search for a user within the social network and view their data.
2. Update your personal data (previously entered).
3. Log out of the social network.

Semantic Harmony Social Network uses a DHT that allows the storage of information relating to each individual user who accesses the social network (user data).

Some of the APIs described above had to be implemented sequentially, as the result of using one of them is the input of another.

When a user logs in to the social network, all users connected at that moment will receive a notification message. This message is sent thanks to an implementation of direct sending of messages to each user of the social network. This information is known to all connected users.



# 2. Technologies used#

- **Programming language**: Java 8
- **DHT management**: TOM P2P
- **Software Project Management**: Apache Maven
- **Containerization technology**: Docker
- **Testing**: JUnit v4.11



# 3. Structure of the project

The project is structured as follows:

- `social`
  - `src`
    - `main\java\it\unisa\adc\p2p`
      - `interfaces`
        - `MessageListener.java`
        - `SocialInterface.java`
      - `object`
        - `User.java`
      - `utility`
        - `Functions.java`
        - `Question.java`
      - `App.java`
      - `SocialImplementation.java`
    - `test\java\it\unisa\adc\p2p`
      - `AppTest.java`

​	

1. The organization is mainly divided into 3 categories:

   1. the interfaces used in the project
   2. the classes object (pure java object)
   3. classes with utility functions



## 3.1 Description of the code

The proposed solution starts from well-defined APIs for solving the problem, described below:

```java
/*Interface provided by the teacher containing the methods to be compulsorily implemented.*/
public interface SocialInterface {

	/**
	 * Gets the social network users questions.
	 * 
	 * @return a list of String that is the profile questions.
	 */
	public List<String> getUserProfileQuestions();

	/**
	 * Creates a new user profile key according the user answers.
	 * 
	 * @param _answer a list of answers.
	 * @return a String, the obtained profile key.
	 */
	public String createAuserProfileKey(List<Integer> _answer);

	/**
	 * Joins in the Network. An automatic messages to each potential new friend is
	 * generated.
	 * 
	 * @param _profile_key a String, the user profile key according the user answers
	 * @param _nick_name   a String, the nickname of the user in the network.
	 * @return true if the join success, fail otherwise.
	 */
	public boolean join(String _profile_key, String _nick_name);

	/**
	 * Gets the nicknames of all automatically creates friendships.
	 * 
	 * @return a list of String.
	 */
	public List<String> getFriends();

}
```



To implement this interface, the SocialImplementation.java class has been defined, which in addition to implementing the interface methods, defines other methods that allow on the one hand, to introduce new features, and on the other to ensure and improve the functioning of the methods interface.

```java

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
```

The use of these methods is provided through a main class that allows the use of the social. 

The main class contains the MessageListenerImpl class which implements the MessageListener interface. This class needs to implement the method "parseMessage", which will be invoked whenever the peer receives a new message sent directly by other peers.

```java
 class MessageListenerImpl implements MessageListener {
            int peerid;

            public MessageListenerImpl(int peerid) {
                this.peerid = peerid;

            }

            public Object parseMessage(Object obj) {

                TextIO textIO = TextIoFactory.getTextIO();
                TextTerminal terminal = textIO.getTextTerminal();
                terminal.printf(obj + "\n");
                return "success";

            }

        }
```

Its behavior is elementary: it reports the message on the terminal. In particular, it is used to send a notification of join to the social by a peer to all peers connected at that moment.

Finally, it is possible to use some of the features implemented several times, thanks to a basic menu created ad hoc.

![image-20210131183613193](C:\Users\danie\AppData\Roaming\Typora\typora-user-images\image-20210131183613193.png)

For each of the visible options, a series of method calls is defined that allow you to perform this operation.

Entering 1 will return a list of nicknames of peers (users) friends (friendship has been established by the social network based on interests). By entering 2 you will be able to search for a user by entering his nickname. The entry does not require you to specify upper or lower case characters but only that they are the correct characters (and sometimes numbers). By entering 3 you can re-enter some information such as name, surname and age entered during the first access. Entering 4 will leave the social network.



# 4. Test Cases

The test cases were implemented with JUnit and can be found in `test\java\it\unisa\adc\p2p\AppTest.java`

Here are the test cases of some functions implemented in the project:

- Test case di Join()

```java
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
```



- Test Case leaveNetwork()

```java

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
```



- Test cae getPeersObject()

```java
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
```



- Test case weAreFriends()

```java
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
```

- Action that is performed before each test

```java
 @BeforeAll
    static void initPeers() throws Exception {
        /* Initialization of peers used for testing */
        peer0 = new SocialImplementation(0, "127.0.0.1", new MessageListenerImpl(0));
        peer1 = new SocialImplementation(1, "127.0.0.1", new MessageListenerImpl(1));
        peer2 = new SocialImplementation(2, "127.0.0.1", new MessageListenerImpl(2));
        peer3 = new SocialImplementation(3, "127.0.0.1", new MessageListenerImpl(3));

    }
```





# 5. Dipendenze

Here are the dependencies defined in the pom.xml file necessary for both the project build and the testing phase.

```xml
 <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>

 <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.5.2</version>
        </dependency>

    <dependency>
			<groupId>net.tomp2p</groupId>
			<artifactId>tomp2p-all</artifactId>
			<version>5.0-Beta8</version>
		</dependency>

	<dependency>
			<groupId>org.beryx</groupId>
			<artifactId>text-io</artifactId>
			<version>3.3.0</version>
		</dependency>

		<dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>

		<dependency>
			<groupId>args4j</groupId>
			<artifactId>args4j</artifactId>
			<version>2.33</version>
		</dependency>
  </dependencies>
```



# 6. Docker

This is the DockerFile defined for Semantic Harmony Social Network:

```dockerfile
FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/daniele321b/daniele_cioffi_adc_2020.git

FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=0 /app/daniele_cioffi_adc_2020/social /app
RUN mvn package

FROM openjdk:8-jre-alpine
WORKDIR /app
ENV MASTERIP=127.0.0.1
ENV ID=0
COPY --from=1 /app/target/social-1.0-SNAPSHOT-jar-with-dependencies.jar /app

CMD /usr/bin/java -jar social-1.0-SNAPSHOT-jar-with-dependencies.jar -m $MASTERIP -id $ID
```

The basic steps to run this application in a Docker container:

- clone of the repository (https://github.com/daniele321b/daniele_cioffi_adc_2020.git):

  ```
  git clone https://github.com/daniele321b/daniele_cioffi_adc_2020.git
  ```

- open a terminal e move to the folder containing the DockerFile you just downloaded:

  ```
  cd daniele_cioffi_adc_2020/social
  ```

- build the docker container:

  ```
  docker build --no-cache -t social-p2p .
  ```

- execute the master peer

  ```
  docker run -i --name MASTER-PEER -e MASTERIP="127.0.0.1" -e ID=0 social-p2p
  ```

- execute another peer (generico):

  - for this operation it is necessary to recover the actual IP address of the container on which we have previously launched the master peer. To do this:

    - verify the ID of docker container in execution:

      ```
      docker ps
      ```

    - verify the information of master peer:

      ```
      docker inspect <master peer ID>
      ```

  - At this point we can find the ip address under the heading "Ip-address" of the .json resulting from the command docker inspect.

    The command to execute is therefore the following:

```
docker run -i --name PEER-1 -e masterip="ip-container" -e ID=1 social-p2p
```

To launch other peers just execute the last block by entering a different ID at each launch.



# 7. Conclusion and future developments

The realization of this project has expanded and strengthened my theoretical and applicative knowledge regarding P2P networks. To the concepts described and demonstrated by the teacher during the course, I was able to feel the behavior of a P2P network by hand with the creation of an application.

Normally, this project can definitely be improved in all respects. In the future, new features or a GUI could be implemented that makes the application more user-friendly, abandoning the raw structure created for educational purposes.
