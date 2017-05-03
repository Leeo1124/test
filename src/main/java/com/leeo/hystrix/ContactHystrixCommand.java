package com.leeo.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.apache.http.client.fluent.Request;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactHystrixCommand extends HystrixCommand<Contact> {
    private static Logger logger = LoggerFactory.getLogger(ContactHystrixCommand.class);
    private String customerId;

    public ContactHystrixCommand(String customerId) {
        super(HystrixCommandGroupKey.Factory.asKey("Contact"));
        this.customerId = customerId;
    }

    @Override
    public Contact run() throws Exception {
        logger.info("Get contact for customer {}", this.customerId);
        String response = Request.Get("http://localhost:9090/customer/" + this.customerId + "/contact")
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute()
                .returnContent()
                .asString();

        return new ObjectMapper().readValue(response, Contact.class);
    }

    @Override
    protected Contact getFallback() {
        logger.info("Met error, using fallback value: {}", this.customerId);
        return null;
    }
    
    public static void main(String[] args) {
        Contact contact = new ContactHystrixCommand("customerId").execute();
        System.out.println("----result: "+contact);
    }
}