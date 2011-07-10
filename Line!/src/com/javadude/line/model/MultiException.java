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

import java.util.Collections;
import java.util.List;

/**
 * A non-checked wrapper exception that can wrap multiple exceptions
 * @author Scott Stanchfield
 */
public class MultiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	// a list of causes
	private List<Throwable> causes;
	
	/**
	 * Create a MultiException.
	 * @param causes a list of causes to track
	 */
	public MultiException(Throwable rootCause, List<Throwable> causes) {
		super(rootCause);
		this.causes = causes;
	}

	/**
	 * Create a MultiException.
	 * @param message a textual message to describe the exception
	 * @param causes a list of causes to track
	 */
	public MultiException(String message, Throwable rootCause, List<Throwable> causes) {
		super(message, rootCause);
		this.causes = causes;
	}
	
	/**
	 * Get the list of causes for the exception.
	 * If you only care about one cause, just call getCause()
	 * @return the list of causes.
	 */
	public List<Throwable> getAdditionalCauses() {
		return Collections.unmodifiableList(causes);
	}
}
