/**
 * JLibs: Common Utilities for Java
 * Copyright (C) 2009  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package jlibs.xml.sax.sniff.model;

import javax.xml.namespace.NamespaceContext;

/**
 * @author Santhosh Kumar T
 */
public class Root extends Node{
    public volatile boolean using;
    
    public NamespaceContext nsContext;
    
    public Root(NamespaceContext nsContext){
        root = this;
        this.nsContext = nsContext;
        hits.totalHits = new HitManager();
    }

    @Override
    public boolean equivalent(Node node){
        return node.getClass()==getClass();
    }

    @Override
    public String toString(){
        return "Root";
    }

    /*-------------------------------------------------[ Reset ]---------------------------------------------------*/

    @Override
    public void reset(){
        hits.reset();
        super.reset();
        using = false;
    }
}
