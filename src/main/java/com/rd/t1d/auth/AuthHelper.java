package com.rd.t1d.auth;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthHelper {

    private FirebaseAuth firebaseAuth;

    //@Cacheable(value = "authCache", key = "#idToken")
    public String verifyAuth(String idToken) throws FirebaseAuthException {
        FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(idToken);
        //log.info("verified idToken for the user: " + token.getEmail());
        return token.getEmail();
    }

    private FirebaseAuth getFirebaseAuthInstance(){
        if(firebaseAuth != null){
            return firebaseAuth;
        }
        synchronized (this){
            if(null == firebaseAuth){
                return FirebaseAuth.getInstance();
            }else{
                return firebaseAuth;
            }
        }
    }
}
