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
    public static void searchFriend(SocialImplementation peer) throws Exception {
        // String key = new String();
        String id = new String();

        // get firends

        for (int i = 0; i < 1000; i++) {
            id = "id" + i;

            try {
                User user0 = peer.get(id);
                System.out.println("userName " + user0.userName);

                if (Functions.weAreFriends(peer._user, user0)) {
                    System.out.println(peer._user.userName + " AND " + user0.userName + "Are friend...");
                    peer._user.friends.add(user0.userName);
                    SocialImplementation peer1 = new SocialImplementation(i, "127.0.0.1");
                    peer1._user.friends.add(peer._user.userName);
                }

            } catch (NoSuchElementException elementException) {
                // System.out.println("NO ID");
            }

        }

    }

}
