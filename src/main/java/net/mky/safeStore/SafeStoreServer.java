/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import org.rapidoid.annotation.Valid;
import org.rapidoid.jpa.JPA;
import org.rapidoid.setup.App;
import org.rapidoid.setup.On;

/**
 *
 * @author mkfs
 */
public class SafeStoreServer {

    public static void main(String... args) {
        // On GET /size return the length of the "msg" parameter
        On.get("/size").json((String msg) -> msg.length());

    }
}
