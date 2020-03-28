/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.setup.On;

/**
 *
 * @author mkfs
 */
public class Server {
    
    public static void main(String... args){
        On.post("/text").plain((req, resp)-> print( req));
    }
    
    public static String print(Req req){
        System.out.println(new String(req.body()));
        return "Done";
}
}
