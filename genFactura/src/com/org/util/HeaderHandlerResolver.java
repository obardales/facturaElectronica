/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.util;

/**
 *
 * @author oswaldo
 */
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class HeaderHandlerResolver implements HandlerResolver {
    private String vruc;
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlerChain = new ArrayList<Handler>();
        HeaderHandler hh = new HeaderHandler();
        hh.setVruc(vruc);
        handlerChain.add(hh);
        return handlerChain;
    }

    public String getVruc() {
        return vruc;
    }

    public void setVruc(String vruc) {
        this.vruc = vruc;
    }
    
}
