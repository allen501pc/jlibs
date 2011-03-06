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

package jlibs.core.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;

/**
 * @author Santhosh Kumar T
 */
public class Pipe{
    private InputChannel input;
    private OutputChannel output;

    public Pipe(InputChannel input, OutputChannel output){
        this.input = input;
        this.output = output;
    }

    public InputChannel input(){
        return input;
    }

    public OutputChannel output(){
        return output;
    }

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    public void start() throws IOException{
        IOHandler ioHandler = new IOHandler();
        input.setHandler(ioHandler);
        output.setHandler(ioHandler);
        input.addInterest();
    }

    private class IOHandler implements InputHandler, OutputHandler{
        @Override
        public void onRead(InputChannel input){
            Channel current = input;
            try{
                buffer.clear();
                int read = input.read(buffer);
                if(read==-1)
                    handler.finished(Pipe.this);
                else if(read==0)
                    input.addInterest();
                else if(read>0){
                    buffer.flip();
                    current = output;
                    output.addWriteInterest();
                }
            }catch(IOException ex){
                handler.onIOException(Pipe.this, current, ex);
            }
        }

        @Override
        public void onTimeout(InputChannel input){
            handler.onTimeout(Pipe.this, input);
        }

        @Override
        public void onWrite(OutputChannel output){
            try{
                output.write(buffer);
            }catch(IOException ex){
                handler.onIOException(Pipe.this, output, ex);
            }
            try{
                if(output.status()==OutputChannel.Status.NEEDS_OUTPUT)
                    output.addStatusInterest();
                else
                    input.addInterest();
            }catch(IOException ex){
                handler.onIOException(Pipe.this, input, ex);
            }
        }

        @Override
        public void onTimeout(OutputChannel output){
            handler.onTimeout(Pipe.this, output);
        }

        @Override
        public void onIOException(OutputChannel output, IOException ex){
            handler.onIOException(Pipe.this, output, ex);
        }

        @Override
        public void onStatus(OutputChannel output){
            try{
                input.addInterest();
            }catch(IOException ex){
                handler.onIOException(Pipe.this, input, ex);
            }
        }
    }

    private Handler handler;
    public void setHandler(Handler handler){
        this.handler = handler;
    }

    private interface Handler{
        public void onTimeout(Pipe pipe, Channel channel);
        public void onIOException(Pipe pipe, Channel channel, IOException ex);
        public void finished(Pipe pipe);
    }
}
