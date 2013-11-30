package com.github.drinking_buddies.webservices.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RestServlet extends HttpServlet {  
    public enum Format {
        JSON("application/json");
        
        Format(String mimeType) {
            this.mimeType = mimeType;
        }
        String mimeType;
    }
    private Format format;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String outputFormat = req.getParameter("format");
        if (outputFormat == null || outputFormat.toLowerCase().equals("json"))
            format = Format.JSON;
        
        String pathInfo = req.getPathInfo();
        
        resp.setContentType(format.mimeType);
        streamOutput(resp.getOutputStream(), format, pathInfo);
        resp.getOutputStream().flush();  
    }
    
    public abstract void streamOutput(OutputStream os, Format format, String pathInfo) throws IOException;

    @SuppressWarnings("unchecked")
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
}
