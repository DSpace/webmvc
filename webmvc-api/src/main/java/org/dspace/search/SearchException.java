/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.search;

/**
 * Just a quick SearchException class to give us the relevant data type
 */
public class SearchException extends Exception
{

    public SearchException()
    {
        super();
    }

    public SearchException(String message)
    {
        super(message);
    }

	public SearchException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SearchException(Throwable cause)
	{
		super(cause);
	}

}
