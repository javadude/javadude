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

import java.util.ArrayList;
import java.util.List;

public class InputBufferEventSupport {
    private List<InputBufferListener> inputBufferListeners;
    private InputBufferEvent  inputBufferEvent;
    protected static final int CONSUME=0;
    protected static final int LA=1;
    protected static final int MARK=2;
    protected static final int REWIND=3;


    public InputBufferEventSupport(Object source) {
        inputBufferEvent = new InputBufferEvent(source);
    }
    public void addInputBufferListener(InputBufferListener l) {
        if (inputBufferListeners == null) {
	        inputBufferListeners = new ArrayList<InputBufferListener>();
        }
        inputBufferListeners.add(l);
    }
    public void fireConsume(char c) {
        inputBufferEvent.setValues(InputBufferEvent.CONSUME, c, 0);
        fireEvents(CONSUME, inputBufferListeners);
    }
    public void fireEvent(int type, ListenerBase l) {
        switch(type) {
            case CONSUME: ((InputBufferListener)l).inputBufferConsume(inputBufferEvent); break;
            case LA:      ((InputBufferListener)l).inputBufferLA(inputBufferEvent); break;
            case MARK:    ((InputBufferListener)l).inputBufferMark(inputBufferEvent); break;
            case REWIND:  ((InputBufferListener)l).inputBufferRewind(inputBufferEvent); break;
            default:
                throw new IllegalArgumentException("bad type "+type+" for fireEvent()");
        }
    }
    public void fireEvents(int type, List<InputBufferListener> listeners) {
        List<InputBufferListener> targets=null;

        synchronized (this) {
            if (listeners == null) {
	            return;
            }
            targets = new ArrayList<InputBufferListener>(listeners);
        }

        for (InputBufferListener l : targets) {
            fireEvent(type, l);
        }
    }
    public void fireLA(char c, int la) {
        inputBufferEvent.setValues(InputBufferEvent.LA, c, la);
        fireEvents(LA, inputBufferListeners);
    }
    public void fireMark(int pos) {
        inputBufferEvent.setValues(InputBufferEvent.MARK, ' ', pos);
        fireEvents(MARK, inputBufferListeners);
    }
    public void fireRewind(int pos) {
        inputBufferEvent.setValues(InputBufferEvent.REWIND, ' ', pos);
        fireEvents(REWIND, inputBufferListeners);
    }
    public List<InputBufferListener> getInputBufferListeners() {
        return inputBufferListeners;
    }
    protected void refresh(List<InputBufferListener> listeners) {
        List<InputBufferListener> v;
        synchronized (listeners) {
            v = new ArrayList<InputBufferListener>(listeners);
        }
        for (InputBufferListener l : v) {
	        l.refresh();
        }
    }
    public void refreshListeners() {
        refresh(inputBufferListeners);
    }
    public void removeInputBufferListener(InputBufferListener l) {
        if (inputBufferListeners != null) {
	        inputBufferListeners.remove(l);
        }
    }
}
