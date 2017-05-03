package com.leeo.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.misc.BASE64Decoder;

/**
 * HTTP基本认证(Basic Authentication)
 *
 */
@Controller
@RequestMapping("/basicAuth")
public class HttpBasicAuthController {

    private static final Logger logger = LoggerFactory.getLogger(HttpBasicAuthController.class);

    @RequestMapping(method = RequestMethod.GET, value = "queryTask")
    public void queryTask(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String sessionAuth = (String) request.getSession().getAttribute("auth");

        if (sessionAuth != null) {
            HttpBasicAuthController.logger.info("this is next step");
            this.nextStep(request, response);

        } else {
            if (!HttpBasicAuthController.checkHeaderAuth(request)) {
                response.setStatus(401);
                response.setHeader("Cache-Control", "no-store");
                response.setDateHeader("Expires", 0);
                response.setHeader("WWW-authenticate", "Basic Realm=\"请输入管理员密码\"");
            }
        }
    }

    private static boolean checkHeaderAuth(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        logger.info("auth encoded in base64 is " + HttpBasicAuthController.getFromBASE64(auth));

        if (auth != null && auth.length() > 6) {
            auth = auth.substring(6, auth.length());

            final String decodedAuth = HttpBasicAuthController.getFromBASE64(auth);
            logger.info("auth decoded from base64 is " + decodedAuth);

            if (decodedAuth.split(":").length < 2) {
                return false;
            }

            final String username = decodedAuth.split(":")[0];
            final String password = decodedAuth.split(":")[1];
            logger.info("----------username：" + username);
            logger.info("----------password：" + password);

            request.getSession().setAttribute("auth", decodedAuth);
            return true;
        } else {
            return false;
        }
    }

    private static String getFromBASE64(String s) {
        if (s == null) {
            return null;
        }
        final BASE64Decoder decoder = new BASE64Decoder();
        try {
            final byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (final Exception e) {
            return null;
        }
    }

    public void nextStep(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final PrintWriter pw = response.getWriter();
        pw.println("<html> next step, authentication is : "+ request.getSession().getAttribute("auth") + "<br>");
        pw.println("<br></html>");
        pw.flush();
        pw.close();
    }

    @RequestMapping(method = RequestMethod.GET, value = "queryTask2")
    public void queryTask2(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final HttpSession session = request.getSession();
        String user = (String) session.getAttribute("username");
        String pass;
        if (user == null) {
            try {
                response.setCharacterEncoding("UTF-8");
                final PrintWriter out = response.getWriter();
                final String authorization = request.getHeader("authorization");
                if (authorization == null || authorization.equals("")) {
                    response.setStatus(401);
                    response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                    out.print("对不起你没有权限！！");

                    return;
                }

                final String userAndPass = new String(new BASE64Decoder().decodeBuffer(authorization.split(" ")[1]));
                if (userAndPass.split(":").length < 2) {
                    response.setStatus(401);
                    response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                    out.print("对不起你没有权限！！");

                    return;
                }

                user = userAndPass.split(":")[0];
                pass = userAndPass.split(":")[1];
                logger.info("----------username：" + user);
                logger.info("----------password：" + pass);

                if (user.equals("111") && pass.equals("111")) {
                    session.setAttribute("username", user);
                    final RequestDispatcher dispatcher = request.getRequestDispatcher("index");
                    dispatcher.forward(request, response);
                } else {
                    response.setStatus(401);
                    response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                    out.print("对不起你没有权限！！");
                }
            } catch (final Exception ex) {
                ex.printStackTrace();
            }

        } else {
            final RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        }
    }
}
