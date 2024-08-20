package com.georgejrdev.auxiliar.reload;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;



public class SimpleHttpServer{
 
    private Server server;

    public SimpleHttpServer(int port, String resourceBase){
        server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");   
        context.setResourceBase(resourceBase);
        context.addServlet(DefaultServlet.class, "/");
        server.setHandler(context);
    }


    public void start() throws Exception {
        server.start();
        System.out.println("Server started at http://localhost:" + server.getURI().getPort());
        server.join();
    }


    public void stop() throws Exception {
        server.stop();
    }
}
