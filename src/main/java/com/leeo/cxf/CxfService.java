package com.leeo.cxf;

import javax.jws.WebService;

/**
 * WebService接口
 * 
 */
@WebService
public interface CxfService {

    public String sayHello(String username);

}
