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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadude.antxr.collections.impl.BitSet;


/** A class to assist in firing parser events
 *  NOTE: I intentionally _did_not_ synchronize the event firing and
 *        add/remove listener methods.  This is because the add/remove should
 *        _only_ be called by the parser at its start/end, and the _same_thread_
 *        should be performing the parsing.  This should help performance a tad...
 */
public class ParserEventSupport {
    private Map<ListenerBase, Integer> doneListeners;
    private List<ParserMatchListener> matchListeners;
    private List<MessageListener> messageListeners;
    private List<ParserTokenListener> tokenListeners;
    private List<TraceListener> traceListeners;
    private List<SemanticPredicateListener> semPredListeners;
    private List<SyntacticPredicateListener> synPredListeners;
    private List<NewLineListener> newLineListeners;
    private ParserMatchEvent        matchEvent;
    private MessageEvent            messageEvent;
    private ParserTokenEvent        tokenEvent;
    private SemanticPredicateEvent  semPredEvent;
    private SyntacticPredicateEvent synPredEvent;
    private TraceEvent              traceEvent;
    private NewLineEvent            newLineEvent;
    private ParserController        controller;
    protected static final int CONSUME=0;
    protected static final int ENTER_RULE=1;
    protected static final int EXIT_RULE=2;
    protected static final int LA=3;
    protected static final int MATCH=4;
    protected static final int MATCH_NOT=5;
    protected static final int MISMATCH=6;
    protected static final int MISMATCH_NOT=7;
    protected static final int REPORT_ERROR=8;
    protected static final int REPORT_WARNING=9;
    protected static final int SEMPRED=10;
    protected static final int SYNPRED_FAILED=11;
    protected static final int SYNPRED_STARTED=12;
    protected static final int SYNPRED_SUCCEEDED=13;
    protected static final int NEW_LINE=14;
    protected static final int DONE_PARSING=15;
    private int ruleDepth = 0;


    public ParserEventSupport(Object source) {
        matchEvent   = new ParserMatchEvent(source);
        messageEvent = new MessageEvent(source);
        tokenEvent   = new ParserTokenEvent(source);
        traceEvent   = new TraceEvent(source);
        semPredEvent = new SemanticPredicateEvent(source);
        synPredEvent = new SyntacticPredicateEvent(source);
        newLineEvent = new NewLineEvent(source);
    }
    public void addDoneListener(ListenerBase l) {
        if (doneListeners == null) {
	        doneListeners = new HashMap<ListenerBase, Integer>();
        }
        Integer i = doneListeners.get(l);
        int val;
        if (i != null) {
	        val = i.intValue() + 1;
        } else {
	        val = 1;
        }
        doneListeners.put(l, new Integer(val));
    }
    public void addMessageListener(MessageListener l) {
        if (messageListeners == null) {
	        messageListeners = new ArrayList<MessageListener>();
        }
        messageListeners.add(l);
        addDoneListener(l);
    }
    public void addNewLineListener(NewLineListener l) {
        if (newLineListeners == null) {
	        newLineListeners = new ArrayList<NewLineListener>();
        }
        newLineListeners.add(l);
        addDoneListener(l);
    }
    public void addParserListener(ParserListener l) {
        if (l instanceof ParserController) {
            ((ParserController)l).setParserEventSupport(this);
            controller = (ParserController)l;
        }
        addParserMatchListener(l);
        addParserTokenListener(l);

        addMessageListener(l);
        addTraceListener(l);
        addSemanticPredicateListener(l);
        addSyntacticPredicateListener(l);
    }
    public void addParserMatchListener(ParserMatchListener l) {
        if (matchListeners == null) {
	        matchListeners = new ArrayList<ParserMatchListener>();
        }
        matchListeners.add(l);
        addDoneListener(l);
    }
    public void addParserTokenListener(ParserTokenListener l) {
        if (tokenListeners == null) {
	        tokenListeners = new ArrayList<ParserTokenListener>();
        }
        tokenListeners.add(l);
        addDoneListener(l);
    }
    public void addSemanticPredicateListener(SemanticPredicateListener l) {
        if (semPredListeners == null) {
	        semPredListeners = new ArrayList<SemanticPredicateListener>();
        }
        semPredListeners.add(l);
        addDoneListener(l);
    }
    public void addSyntacticPredicateListener(SyntacticPredicateListener l) {
        if (synPredListeners == null) {
	        synPredListeners = new ArrayList<SyntacticPredicateListener>();
        }
        synPredListeners.add(l);
        addDoneListener(l);
    }
    public void addTraceListener(TraceListener l) {
        if (traceListeners == null) {
	        traceListeners = new ArrayList<TraceListener>();
        }
        traceListeners.add(l);
        addDoneListener(l);
    }
    public void fireConsume(int value) {
        tokenEvent.setValues(ParserTokenEvent.CONSUME, 1, value);
        fireEvents(CONSUME, tokenListeners);
    }
    public void fireDoneParsing() {
        traceEvent.setValues(TraceEvent.DONE_PARSING, 0,0,0);

        Map<ListenerBase, Integer> targets=null;

        synchronized (this) {
            if (doneListeners == null) {
	            return;
            }
            targets = new HashMap<ListenerBase, Integer>(doneListeners);
        }

        for (ListenerBase l : targets.keySet()) {
            fireEvent(DONE_PARSING, l);
        }
        if (controller != null) {
	        controller.checkBreak();
        }
    }
    public void fireEnterRule(int ruleNum, int guessing, int data) {
        ruleDepth++;
        traceEvent.setValues(TraceEvent.ENTER, ruleNum, guessing, data);
        fireEvents(ENTER_RULE, traceListeners);
    }
    public void fireEvent(int type, ListenerBase l) {
        switch(type) {
            case CONSUME:    ((ParserTokenListener)l).parserConsume(tokenEvent); break;
            case LA:         ((ParserTokenListener)l).parserLA(tokenEvent);      break;

            case ENTER_RULE: ((TraceListener)l).enterRule(traceEvent);           break;
            case EXIT_RULE:  ((TraceListener)l).exitRule(traceEvent);            break;

            case MATCH:        ((ParserMatchListener)l).parserMatch(matchEvent);       break;
            case MATCH_NOT:    ((ParserMatchListener)l).parserMatchNot(matchEvent);    break;
            case MISMATCH:     ((ParserMatchListener)l).parserMismatch(matchEvent);    break;
            case MISMATCH_NOT: ((ParserMatchListener)l).parserMismatchNot(matchEvent); break;

            case SEMPRED:      ((SemanticPredicateListener)l).semanticPredicateEvaluated(semPredEvent); break;

            case SYNPRED_STARTED:   ((SyntacticPredicateListener)l).syntacticPredicateStarted(synPredEvent);   break;
            case SYNPRED_FAILED:    ((SyntacticPredicateListener)l).syntacticPredicateFailed(synPredEvent);    break;
            case SYNPRED_SUCCEEDED: ((SyntacticPredicateListener)l).syntacticPredicateSucceeded(synPredEvent); break;

            case REPORT_ERROR:   ((MessageListener)l).reportError(messageEvent);   break;
            case REPORT_WARNING: ((MessageListener)l).reportWarning(messageEvent); break;

            case DONE_PARSING: l.doneParsing(traceEvent); break;
            case NEW_LINE:     ((NewLineListener)l).hitNewLine(newLineEvent); break;

            default:
                throw new IllegalArgumentException("bad type "+type+" for fireEvent()");
        }
    }
    public void fireEvents(int type, List<? extends ListenerBase> listeners) {
        if (listeners != null) {
	        for (ListenerBase l : listeners) {
                fireEvent(type, l);
            }
        }
        if (controller != null) {
	        controller.checkBreak();
        }
    }
    public void fireExitRule(int ruleNum, int guessing, int data) {
        traceEvent.setValues(TraceEvent.EXIT, ruleNum, guessing, data);
        fireEvents(EXIT_RULE, traceListeners);
        ruleDepth--;
        if (ruleDepth == 0) {
	        fireDoneParsing();
        }
    }
    public void fireLA(int k, int la) {
        tokenEvent.setValues(ParserTokenEvent.LA, k, la);
        fireEvents(LA, tokenListeners);
    }
    public void fireMatch(char c, int guessing) {
        matchEvent.setValues(ParserMatchEvent.CHAR, c, new Character(c), null, guessing, false, true);
        fireEvents(MATCH, matchListeners);
    }
    public void fireMatch(char value, BitSet b, int guessing) {
        matchEvent.setValues(ParserMatchEvent.CHAR_BITSET, value, b, null, guessing, false, true);
        fireEvents(MATCH, matchListeners);
    }
    public void fireMatch(char value, String target, int guessing) {
        matchEvent.setValues(ParserMatchEvent.CHAR_RANGE, value, target, null, guessing, false, true);
        fireEvents(MATCH, matchListeners);
    }
    public void fireMatch(int value, BitSet b, String text, int guessing) {
        matchEvent.setValues(ParserMatchEvent.BITSET, value, b, text, guessing, false, true);
        fireEvents(MATCH, matchListeners);
    }
    public void fireMatch(int n, String text, int guessing) {
        matchEvent.setValues(ParserMatchEvent.TOKEN, n, new Integer(n), text, guessing, false, true);
        fireEvents(MATCH, matchListeners);
    }
    public void fireMatch(String s, int guessing) {
        matchEvent.setValues(ParserMatchEvent.STRING, 0, s, null, guessing, false, true);
        fireEvents(MATCH, matchListeners);
    }
    public void fireMatchNot(char value, char n, int guessing) {
        matchEvent.setValues(ParserMatchEvent.CHAR, value, new Character(n), null, guessing, true, true);
        fireEvents(MATCH_NOT, matchListeners);
    }
    public void fireMatchNot(int value, int n, String text, int guessing) {
        matchEvent.setValues(ParserMatchEvent.TOKEN, value, new Integer(n), text, guessing, true, true);
        fireEvents(MATCH_NOT, matchListeners);
    }
    public void fireMismatch(char value, char n, int guessing) {
        matchEvent.setValues(ParserMatchEvent.CHAR, value, new Character(n), null, guessing, false, false);
        fireEvents(MISMATCH, matchListeners);
    }
    public void fireMismatch(char value, BitSet b, int guessing) {
        matchEvent.setValues(ParserMatchEvent.CHAR_BITSET, value, b, null, guessing, false, true);
        fireEvents(MISMATCH, matchListeners);
    }
    public void fireMismatch(char value, String target, int guessing) {
        matchEvent.setValues(ParserMatchEvent.CHAR_RANGE, value, target, null, guessing, false, true);
        fireEvents(MISMATCH, matchListeners);
    }
    public void fireMismatch(int value, int n, String text, int guessing) {
        matchEvent.setValues(ParserMatchEvent.TOKEN, value, new Integer(n), text, guessing, false, false);
        fireEvents(MISMATCH, matchListeners);
    }
    public void fireMismatch(int value, BitSet b, String text, int guessing) {
        matchEvent.setValues(ParserMatchEvent.BITSET, value, b, text, guessing, false, true);
        fireEvents(MISMATCH, matchListeners);
    }
    public void fireMismatch(String value, String text, int guessing) {
        matchEvent.setValues(ParserMatchEvent.STRING, 0, text, value, guessing, false, true);
        fireEvents(MISMATCH, matchListeners);
    }
    public void fireMismatchNot(char value, char c, int guessing) {
        matchEvent.setValues(ParserMatchEvent.CHAR, value, new Character(c), null, guessing, true, true);
        fireEvents(MISMATCH_NOT, matchListeners);
    }
    public void fireMismatchNot(int value, int n, String text, int guessing) {
        matchEvent.setValues(ParserMatchEvent.TOKEN, value, new Integer(n), text, guessing, true, true);
        fireEvents(MISMATCH_NOT, matchListeners);
    }
    public void fireNewLine(int line) {
        newLineEvent.setValues(line);
        fireEvents(NEW_LINE, newLineListeners);
    }
    public void fireReportError(Exception e) {
        messageEvent.setValues(MessageEvent.ERROR, e.toString());
        fireEvents(REPORT_ERROR, messageListeners);
    }
    public void fireReportError(String s) {
        messageEvent.setValues(MessageEvent.ERROR, s);
        fireEvents(REPORT_ERROR, messageListeners);
    }
    public void fireReportWarning(String s) {
        messageEvent.setValues(MessageEvent.WARNING, s);
        fireEvents(REPORT_WARNING, messageListeners);
    }
    public boolean fireSemanticPredicateEvaluated(int type, int condition, boolean result, int guessing) {
        semPredEvent.setValues(type, condition, result, guessing);
        fireEvents(SEMPRED, semPredListeners);
        return result;
    }
    public void fireSyntacticPredicateFailed(int guessing) {
        synPredEvent.setValues(0, guessing);
        fireEvents(SYNPRED_FAILED, synPredListeners);
    }
    public void fireSyntacticPredicateStarted(int guessing) {
        synPredEvent.setValues(0, guessing);
        fireEvents(SYNPRED_STARTED, synPredListeners);
    }
    public void fireSyntacticPredicateSucceeded(int guessing) {
        synPredEvent.setValues(0, guessing);
        fireEvents(SYNPRED_SUCCEEDED, synPredListeners);
    }
    protected <Type extends ListenerBase> void refresh(List<Type> listeners) {
        List<Type> v;
        synchronized (listeners) {
            v = new ArrayList<Type>(listeners);
        }
        for (Type l : v) {
	        l.refresh();
        }
    }
    public void refreshListeners() {
        refresh(matchListeners);
        refresh(messageListeners);
        refresh(tokenListeners);
        refresh(traceListeners);
        refresh(semPredListeners);
        refresh(synPredListeners);
    }
    public void removeDoneListener(ListenerBase l) {
        if (doneListeners == null) {
	        return;
        }
        Integer i = doneListeners.get(l);
        int val=0;
        if (i != null) {
	        val = i.intValue() - 1;
        }

        if (val == 0) {
	        doneListeners.remove(l);
        } else {
	        doneListeners.put(l, new Integer(val));
        }
    }
    public void removeMessageListener(MessageListener l) {
        if (messageListeners != null) {
	        messageListeners.remove(l);
        }
        removeDoneListener(l);
    }
    public void removeNewLineListener(NewLineListener l) {
        if (newLineListeners != null) {
	        newLineListeners.remove(l);
        }
        removeDoneListener(l);
    }
    public void removeParserListener(ParserListener l) {
        removeParserMatchListener(l);
        removeMessageListener(l);
        removeParserTokenListener(l);
        removeTraceListener(l);
        removeSemanticPredicateListener(l);
        removeSyntacticPredicateListener(l);
    }
    public void removeParserMatchListener(ParserMatchListener l) {
        if (matchListeners != null) {
	        matchListeners.remove(l);
        }
        removeDoneListener(l);
    }
    public void removeParserTokenListener(ParserTokenListener l) {
        if (tokenListeners != null) {
	        tokenListeners.remove(l);
        }
        removeDoneListener(l);
    }
    public void removeSemanticPredicateListener(SemanticPredicateListener l) {
        if (semPredListeners != null) {
	        semPredListeners.remove(l);
        }
        removeDoneListener(l);
    }
    public void removeSyntacticPredicateListener(SyntacticPredicateListener l) {
        if (synPredListeners != null) {
	        synPredListeners.remove(l);
        }
        removeDoneListener(l);
    }
    public void removeTraceListener(TraceListener l) {
        if (traceListeners != null) {
	        traceListeners.remove(l);
        }
        removeDoneListener(l);
    }
}
