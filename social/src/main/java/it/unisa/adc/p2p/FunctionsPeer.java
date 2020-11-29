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

public class FunctionsPeer {
    public static String searchFriend(SocialImplementation my) throws Exception {
        String key = new String();
        String id = new String();
        String namePeer = new String();
        User userS;

        // get firends

        for (int i = 0; i < 1000; i++) {
            id = "id" + i;
            namePeer = my.getUsername(id);
            System.out.println("userName: " + namePeer);
            userS = my.get(id);
            // System.out.println("Stringhe da conforntare \n ");
            // Functions.showAnswers(my._user._answers);
            // Functions.showAnswers(userS._answers);
            if (Functions.weAreFriends(my._user, userS)) {
                System.out.println(my._user.userName + " AND " + userS.userName + "Are friend...");
                my._user.friends.add(namePeer);
                SocialImplementation newPeer = new SocialImplementation(i, "127.0.0.1");

            } else {
                System.out.println(my._user.userName + " AND " + userS.userName + "Are Not friend...");
            }

        }

        return key;
    }

}
