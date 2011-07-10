//*****************************************************************************/
/*                              COPYRIGHT NOTICE                              */
/* Copyright (c) 2009 The Johns Hopkins University/Applied Physics Laboratory */
/*                            All rights reserved.                            */
/*                                                                            */
/* This material may only be used, modified, or reproduced by or for the      */
/* U.S. Government pursuant to the license rights granted under FAR clause    */
/* 52.227-14 or DFARS clauses 252.227-7013/7014.                              */
/*                                                                            */
/* For any other permissions, please contact the Legal Office at JHU/APL.     */
//*****************************************************************************/

package com.javadude.line.model;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>A nice little template-method-based class that handles proper file closing.</p>
 * <h2>Usage</h2>
 * <p>Callers subclass this class as an anonymous inner class, overriding
 *   the doWork() method to do the actual work.</p>
 *   
 * <p>For example, if you wanted to copy lines from one file to another (note - not an
 *    efficient implementation; just meant as an example of AutoFileCloser). You
 *    would use code like the following inside any method:</p>
 * 
 * <pre>
 * new AutoFileCloser() {
 *     protected void doWork() throws Throwable {
 *         FileReader     fr = watch(new FileReader("foo.txt"));
 *         BufferedReader br = watch(new BufferedReader(fr));
 *         FileWriter     fw = watch(new FileWriter("fee.txt"));
 *         BufferedWriter bw = watch(new BufferedWriter(fw));
 *         PrintWriter    pw = watch(new PrintWriter(bw)); 
 *         String line;
 *         while ((line = br.readLine()) != null) {
 *             pw.println(line);
 *         }
 *     }
 * };
 * </pre>
 * 
 * <p>The AutoFileCloser constructor is a template method. It wraps a call to doWork()
 *    in the appropriate try/catch/finally block for proper file close handling.</p>
 * <p>The doWork() method is a hook - the subclass must implement it. Your doWork()
 *    method calls one or two methods in AutoFileCloser:</p>
 * <dl>
 *   <dt><code>watch(Closeable)</code></dt>
 *   <dd>Tells the AutoFileCloser to watch the specified stream/reader/writer. You should
 *       call this on all nested streams/readers/writers as in the above example. This
 *       allows the AutoFileCloser to try to close the nested streams/readers/writers
 *       even if the wrapping streams/readers/writers failed their close, trying its
 *       best to free up file system handles, socket streams, etc. You should not do
 *       something like the following, as it will not allow the AutoFileCloser to
 *       try to close the FileWriter if the PrintWriter close() fails for some reason.
 *       <pre>
 *       // bad!!!
 *       PrintWriter pw = watch(new PrintWriter(new FileWriter("...")));
 *       </pre>
 *   </dd>
 *   <dt><code>setResult(Object)</code></dt>
 *   <dd>If your doWork() method needs to return a value, you can call setResult() passing
 *        the value you want to return. The caller of the AutoFileCloser can then ask for
 *        the result. For example:
 *        <pre>
 *        AutoFileCloser afc = new AutoFileCloser() {
 *            protected void doWork() throws Throwable {
 *                ...
 *                setResult("foo");
 *            }
 *        };
 *        System.out.println(afc.getResult());
 *        </pre>
 *    </dd>
 * </dl>
 * 
 * <h2>Exception handling in your code</h2>
 * <p>AutoFileCloser wraps any exceptions that are thrown in a MultiException.
 * MultiException is an unchecked exception that has a list of causes. During
 * execution, your code can throw an exception, and the close attempts can
 * also throw exceptions. Your exception will be the root cause exception of
 * the MultiException. All exceptions (including yours) will be captured in
 * the "causes" list of MultiException.</p>
 * <p>You can handle the exception using a try/catch at any level in the call chain</p>
 * <pre>
 * try {
 *     new AutoFileCloser() {
 *         ...
 *     };
 * } catch (MultiException e) {
 *     e.printStackTrace();  // includes first exception as cause
 *     for (Throwable t : e.getCauses()) { // all causes
 *         t.printStackTrace();
 *     }
 * }
 * </pre>
 */
public abstract class AutoFileCloser {
	// the list of things we want to close during cleanup
	private List<Closeable> closeables = new LinkedList<Closeable>();
	
	// if doWork() wants to return a result, it can set it here
	private Object result;
	
	/**
	 * <p>Tells the AutoFileCloser which streams/readers/writers to automatically close.</p>
	 * @param closeable the stream/reader/writer to close when finished
	 * @return the stream passed in (allows for easy assignment - see the example in the class description above)
	 */
	protected final <T extends Closeable> T autoClose(T closeable) {
		closeables.add(0, closeable);
		return closeable;
	}
	
	// NOTE: I duplicate this comment from the class so it comes up in the different contexts it would
	//       be used in. This allows eclipse to display javadoc when specifying the class name (in code assist)
	//       and when the constructor is visible for anonymous inner class usage.
	//       Unfortunately this requires dual-maintenance of this comment... Grrrr...
	
	/**
	 * <p>A nice little template-method-based class that handles proper file closing.</p>
	 * <h2>Usage</h2>
	 * <p>Callers subclass this class as an anonymous inner class, overriding
	 *   the doWork() method to do the actual work.</p>
	 *   
	 * <p>For example, if you wanted to copy lines from one file to another (note - not an
	 *    efficient implementation; just meant as an example of AutoFileCloser). You
	 *    would use code like the following inside any method:</p>
	 * 
	 * <pre>
	 * new AutoFileCloser() {
	 *     protected void doWork() throws Throwable {
	 *         FileReader     fr = watch(new FileReader("foo.txt"));
	 *         BufferedReader br = watch(new BufferedReader(fr));
	 *         FileWriter     fw = watch(new FileWriter("fee.txt"));
	 *         BufferedWriter bw = watch(new BufferedWriter(fw));
	 *         PrintWriter    pw = watch(new PrintWriter(bw)); 
	 *         String line;
	 *         while ((line = br.readLine()) != null) {
	 *             pw.println(line);
	 *         }
	 *     }
	 * };
	 * </pre>
	 * 
	 * <p>The AutoFileCloser constructor is a template method. It wraps a call to doWork()
	 *    in the appropriate try/catch/finally block for proper file close handling.</p>
	 * <p>The doWork() method is a hook - the subclass must implement it. Your doWork()
	 *    method calls one or two methods in AutoFileCloser:</p>
	 * <dl>
	 *   <dt><code>watch(Closeable)</code></dt>
	 *   <dd>Tells the AutoFileCloser to watch the specified stream/reader/writer. You should
	 *       call this on all nested streams/readers/writers as in the above example. This
	 *       allows the AutoFileCloser to try to close the nested streams/readers/writers
	 *       even if the wrapping streams/readers/writers failed their close, trying its
	 *       best to free up file system handles, socket streams, etc. You should not do
	 *       something like the following, as it will not allow the AutoFileCloser to
	 *       try to close the FileWriter if the PrintWriter close() fails for some reason.
	 *       <pre>
	 *       // bad!!!
	 *       PrintWriter pw = watch(new PrintWriter(new FileWriter("...")));
	 *       </pre>
	 *   </dd>
	 *   <dt><code>setResult(Object)</code></dt>
	 *   <dd>If your doWork() method needs to return a value, you can call setResult() passing
	 *        the value you want to return. The caller of the AutoFileCloser can then ask for
	 *        the result. For example:
	 *        <pre>
	 *        AutoFileCloser afc = new AutoFileCloser() {
	 *            protected void doWork() throws Throwable {
	 *                ...
	 *                setResult("foo");
	 *            }
	 *        };
	 *        System.out.println(afc.getResult());
	 *        </pre>
	 *    </dd>
	 * </dl>
	 * 
	 * <h2>Exception handling in your code</h2>
	 * <p>AutoFileCloser wraps any exceptions that are thrown in a MultiException.
	 * MultiException is an unchecked exception that has a list of causes. During
	 * execution, your code can throw an exception, and the close attempts can
	 * also throw exceptions. Your exception will be the root cause exception of
	 * the MultiException. All exceptions (including yours) will be captured in
	 * the "causes" list of MultiException.</p>
	 * <p>You can handle the exception using a try/catch at any level in the call chain</p>
	 * <pre>
	 * try {
	 *     new AutoFileCloser() {
	 *         ...
	 *     };
	 * } catch (MultiException e) {
	 *     e.printStackTrace();  // includes first exception as cause
	 *     for (Throwable t : e.getCauses()) { // all causes
	 *         t.printStackTrace();
	 *     }
	 * }
	 * </pre>
	 */
	public AutoFileCloser() {
		List<Throwable> causes = null;
		Throwable pending = null;
		try {
			doWork();
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			pending = t;
		} finally {
			for (Closeable closeable : closeables) {
				try {
					closeable.close();
				} catch (ThreadDeath t) {
					throw t;
				} catch (Throwable t) {
					if (pending == null)
						pending = t;
					else {
						if (causes == null) 
							causes = new ArrayList<Throwable>(1);
						causes.add(t);
					}
				}
			}
			if (pending != null) {
				if (causes != null)
					throw new MultiException(pending, causes);
				else if (pending instanceof RuntimeException)
					throw (RuntimeException) pending;
				else if (pending instanceof Error)
					throw (Error) pending;
				else
					throw new RuntimeException(pending);
			}
		}
	}
	/**
	 * A hook method that your subclasses must override to do the actual work.
	 * @throws Throwable You can throw any exception you'd like.
	 */
	protected abstract void doWork() throws Throwable;
	
	/**
	 * Allows the hook doWork() method to set a result value that the caller can access.
	 * @param result the result value
	 */
	protected void setResult(Object result) {
		this.result = result;
	}

	/**
	 * @return the result that was set by the doWork() method
	 */
	public Object getResult() {
		return result;
	}
}

