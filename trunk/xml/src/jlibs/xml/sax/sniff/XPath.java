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

package jlibs.xml.sax.sniff;

import jlibs.xml.sax.sniff.model.Node;
import jlibs.xml.sax.sniff.model.Predicate;
import jlibs.xml.sax.sniff.model.Function;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * @author Santhosh Kumar T
 */
public class XPath{
    String xpath;
    List<Node> nodes = Collections.emptyList();
    List<Predicate> predicates = new ArrayList<Predicate>();
    int minHits;

    public XPath(String xpath, List<Node> nodes){
        this.xpath = xpath;
        this.nodes = nodes;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public XPath(String xpath, List<Predicate> predicates, boolean dummy){
        this.xpath = xpath;
        this.predicates = predicates;
    }

    public QName resultType(){
        for(Node node: nodes){
            if(node instanceof Function)
                return XPathConstants.STRING;
        }
        return XPathConstants.NODESET;
    }

    @Override
    public String toString(){
        return xpath;
    }
}
