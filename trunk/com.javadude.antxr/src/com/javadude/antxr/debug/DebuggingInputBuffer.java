/*******************************************************************************
 *  Copyright 2008 Scott Stanchfield.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *******************************************************************************/
package com.javadude.antxr.debug;

import java.util.List;

import com.javadude.antxr.CharStreamException;
import com.javadude.antxr.InputBuffer;

public class DebuggingInputBuffer extends InputBuffer {
    private InputBuffer buffer;
    private InputBufferEventSupport inputBufferEventSupport;
    private boolean debugMode = true;


    public DebuggingInputBuffer(InputBuffer buffer) {
        this.buffer = buffer;
        inputBufferEventSupport = new InputBufferEventSupport(this);
    }
    public void addInputBufferListener(InputBufferListener l) {
      inputBufferEventSupport.addInputBufferListener(l);
    }
    @Override
    public void consume() {
        char la = ' ';
        try {la = buffer.LA(1);}
        catch (CharStreamException e) { /* do nothing */ } // vaporize it...
        buffer.consume();
        if (debugMode) {
            inputBufferEventSupport.fireConsume(la);
        }
    }
    @Override
    public void fill(int a) throws CharStreamException {
        buffer.fill(a);
    }
    public List<InputBufferListener> getInputBufferListeners() {
        return inputBufferEventSupport.getInputBufferListeners();
    }
    public boolean isDebugMode() {
        return debugMode;
    }
    @Override
    public boolean isMarked() {
        return buffer.isMarked();
    }
    @Override
    public char LA(int i) throws CharStreamException {
        char la = buffer.LA(i);
        if (debugMode) {
            inputBufferEventSupport.fireLA(la,i);
        }
        return la;
    }
    @Override
    public int mark() {
        int m = buffer.mark();
        inputBufferEventSupport.fireMark(m);
        return m;
    }
    public void removeInputBufferListener(InputBufferListener l) {
      if (inputBufferEventSupport != null) {
        inputBufferEventSupport.removeInputBufferListener(l);
    }
    }
    @Override
    public void rewind(int mark) {
        buffer.rewind(mark);
        inputBufferEventSupport.fireRewind(mark);
    }
    public void setDebugMode(boolean value) {
        debugMode = value;
    }
}
