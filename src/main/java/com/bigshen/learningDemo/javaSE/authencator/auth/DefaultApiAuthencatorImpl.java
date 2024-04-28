package com.bigshen.learningDemo.javaSE.authencator.auth;

import com.bigshen.learningDemo.javaSE.authencator.request.ApiRequest;
import com.bigshen.learningDemo.javaSE.authencator.storage.CredentialStorage;
import com.bigshen.learningDemo.javaSE.authencator.storage.MySqlCredentialStorage;
import com.bigshen.learningDemo.javaSE.authencator.token.AuthToken;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 18:45
 */
public class DefaultApiAuthencatorImpl implements ApiAuthencator {
    private CredentialStorage storage;

    public DefaultApiAuthencatorImpl() {
        storage = new MySqlCredentialStorage();
    }


    @Override
    public void auth(String url) {
        ApiRequest req = ApiRequest.genFakeReq(url);
//    ApiRequest req = new ApiRequest(url);
        AuthToken clientToken = new AuthToken(req.getBaseUrl(), req.getTimeStamp(), req.getToken());
        if (clientToken.isExpired()) {
            throw new RuntimeException("Request has expired!");
        }
        final String password = storage.getPasswordByAppID(req.getAppID());
        AuthToken serverToken = AuthToken.buildAuthToken(req, password);

        if (!clientToken.match(serverToken)) {
            throw new RuntimeException("Client token does not match!");
        }
        System.out.println("pass auth!");
    }
}
