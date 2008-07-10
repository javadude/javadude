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
package com.javadude.antxr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/** PreservingFileWriter only overwrites target if the new file is different.
 Mainly added in order to prevent big and unnecessary recompiles in C++
 projects.
 I/O is buffered.
*/
public class PreservingFileWriter extends FileWriter {
	protected File target_file;	/// the file we intend to write to
	protected File tmp_file;		/// the tmp file we create at first

	public PreservingFileWriter(String file) throws IOException
	{
		super(file+".com.javadude.antxr.tmp");

		// set up File thingy for target..
		target_file = new File(file);

		String parentdirname = target_file.getParent();
		if( parentdirname != null )
	    {
			File parentdir = new File(parentdirname);

			if (!parentdir.exists()) {
                throw new IOException("destination directory of '"+file+"' doesn't exist");
            }
			if (!parentdir.canWrite()) {
                throw new IOException("destination directory of '"+file+"' isn't writeable");
            }
		}
		if( target_file.exists() && ! target_file.canWrite() ) {
            throw new IOException("cannot write to '"+file+"'");
        }

		// and for the temp file
		tmp_file = new File(file+".com.javadude.antxr.tmp");
		// have it nuked at exit
		// RK: this is broken on java 1.4 and
		// is not compatible with java 1.1 (which is a big problem I'm told :) )
		// sigh. Any real language would do this in a destructor ;) ;)
		// tmp_file.deleteOnExit();
	}

	/** Close the file and see if the actual target is different
	 * if so the target file is overwritten by the copy. If not we do nothing
	 */
	@Override
    public void close() throws IOException
	{
		Reader source = null;
		Writer target = null;

		try {
			// close the tmp file so we can access it safely...
			super.close();

			char[] buffer = new char[1024];
			int cnt;

			// target_file != tmp_file so we have to compare and move it..
			if( target_file.length() == tmp_file.length() )
			{
				// Do expensive read'n'compare
				Reader tmp;
				char[] buf2 = new char[1024];

				source = new BufferedReader(new FileReader(tmp_file));
				tmp = new BufferedReader(new FileReader(target_file));
				int cnt1, cnt2;
				boolean equal = true;

				while( equal )
				{
					cnt1 = source.read(buffer,0,1024);
					cnt2 = tmp.read(buf2,0,1024);
					if( cnt1 != cnt2 )
					{
						equal = false;
						break;
					}
					if( cnt1 == -1 ) {
                        break;
                    }
					for( int i = 0; i < cnt1; i++ )
					{
						if( buffer[i] != buf2[i] )
						{
							equal = false;
							break;
						}
					}
				}
				// clean up...
				source.close();
				tmp.close();

				source = tmp = null;

				if( equal ) {
                    return;
                }
			}

			source = new BufferedReader(new FileReader(tmp_file));
			target = new BufferedWriter(new FileWriter(target_file));

			while(true)
			{
				cnt = source.read(buffer,0,1024);
				if( cnt == -1 ) {
                    break;
                }
				target.write(buffer, 0, cnt );
			}
		}
		finally {
			if( source != null )
			{
				try { source.close(); }
				catch( IOException e ) { /* do nothing */ }
			}
			if( target != null )
			{
				try { target.close(); }
				catch( IOException e ) { /* do nothing */	}
			}
			// RK: Now if I'm correct this should be called anytime.
			if( tmp_file != null && tmp_file.exists() )
			{
				tmp_file.delete();
				tmp_file = null;
			}
		}
	}
}
