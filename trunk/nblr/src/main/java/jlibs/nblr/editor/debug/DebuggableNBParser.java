/**
 * JLibs: Common Utilities for Java
 * Copyright (C) 2009  Santhosh Kumar T <santhosh.tekuri@gmail.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package jlibs.nblr.editor.debug;

import jlibs.nbp.Buffer;
import jlibs.nbp.IntStack;
import jlibs.nbp.NBParser;

public abstract class DebuggableNBParser extends NBParser{
    private Debugger debugger;

    protected DebuggableNBParser(Debugger debugger, int maxLookAhead, int startingRule){
        super(maxLookAhead, startingRule);
        this.debugger = debugger;
        debugger.currentNode(ruleStack.peek(), stateStack.peek());
    }

    @Override
    protected final int callRule(int ch) throws Exception{
        int newState = _callRule(ch);
        if(newState!=-1)
            debugger.currentNode(newState);
        return newState;
    }

    protected abstract int _callRule(int ch) throws Exception;

    @Override
    protected void push(int toRule, int stateAfterRule, int stateInsideRule){
        super.push(toRule, stateAfterRule, stateInsideRule);
        if(debugger!=null)
            debugger.currentNode(ruleStack.peek(), stateStack.peek());
    }

    @Override
    protected void pop(){
        super.pop();
        if(!ruleStack.isEmpty())
            debugger.currentNode(ruleStack.peek(), stateStack.peek());
    }

    public IntStack getRuleStack(){
        return ruleStack;
    }

    public IntStack getStateStack(){
        return stateStack;
    }

    public Buffer getBuffer(){
        return buffer;
    }
}
