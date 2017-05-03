package com.leeo.cxf;

import javax.jws.WebService;

/**
* WebService实现类
*
*/
@WebService(endpointInterface = "com.leeo.cxf.CxfService", serviceName = "cxfService")
public class CxfServiceImpl implements CxfService {

    @Override
    public String sayHello(String username) {
        return "hello, " + username;
    }

}
