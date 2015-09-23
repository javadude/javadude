# Dynamic Working Sets for Eclipse. #

# News #
Following another suggested enhancement, I've set up the regex working sets so they will also include closed projects. I may make this an option at some point.

Following a suggested enhancement, I've added M2Eclipse support - you can now set up a working set based on a regular expression for the group and/or artifact ids of a maven project.

# Note on closed projects #
The regular expression working set will include all projects that match the regular expression, regardless if they are open or closed.

The nature and maven working sets require examination of project contents that cannot be performed when a project is closed. These working sets will only include open projects.

# Introduction #

Working sets are a great way to organize your projects in eclipse. However, it's up to you to add/move/remove projects.

I was working with a group, and whenever we checked different groups of projects out, we'd have to do the old "drag 'em into the working sets" dance.

I got tired of this, and dynamic working sets were born.

Dynamic working sets automatically manage their contents based on regular expressions or project natures. Nifty! The eclipse API never ceases to amaze me!

# Installation #

You can grab the dynamic working sets feature at

http://javadude.googlecode.com/svn/trunk/com.javadude.updatesite

If you're not sure how to use the Eclipse Update Manager open the Eclipse Help->Help Contents, and read the section under

Workbench User Guide->Tasks->Updating Features with the update Manager->Installing new features with the update manager.

Choose "JavaDude Automated Working Sets Feature"

Note: If you select the "JavaDude Dynamic Maven Working Set Feature" you must have m2eclipse installed or you will not see the maven working sets appear.

![http://javadude.googlecode.com/svn/wiki/images/p2.gif](http://javadude.googlecode.com/svn/wiki/images/p2.gif)

# Use #

First, we need to define a dynamic working set. I'll use the regular expression working set as an example.

I really like to use the **Top Level Elements->Working Sets** option in the Package Explorer (it's off the little upside-down triangle option menu on the Package Explorer toolbar.) _Unfortunately, if this option is turned on, you can only create Java Working Sets!_. So before you create a dynamic working set, you must turn it off:

![http://javadude.googlecode.com/svn/wiki/images/toplevel.gif](http://javadude.googlecode.com/svn/wiki/images/toplevel.gif)

Now let's define the working set. Choose **Select Working Set...**

![http://javadude.googlecode.com/svn/wiki/images/selectws.gif](http://javadude.googlecode.com/svn/wiki/images/selectws.gif)

Press **New...** and you'll see the New Working Set Dialog

![http://javadude.googlecode.com/svn/wiki/images/newws.gif](http://javadude.googlecode.com/svn/wiki/images/newws.gif)

The JavaDude Dynamic Working Sets feature adds two new options

  * **Regular Expression Working Set**: Projects are automatically added to/removed from the working set based on their names matching a regular expression
  * **Nature Working Set**: Projects are automatically added to/removed from the working set based on their natures

Select "Regular Expression Working Set" and press **Next...**.

![http://javadude.googlecode.com/svn/wiki/images/regexws.gif](http://javadude.googlecode.com/svn/wiki/images/regexws.gif)

Here you specify a label for the working set (which will appear in the package explorer if you have **Top Level Elements->Working Sets** selected), and a regular expression that will select which projects should be included.

If you need help with the regular expression, press control-space when the cursor is in that field. Content assist will appear in the same manner it does in the standard Eclipse find/replace dialog.

_Note: The regular expression must **fully match** the project names. If you want to specify just a prefix, you must end the expression with **.`*`** ._

As you change the regular expression, you'll see the projects in the workspace that match it.

When done with the settings, press **Finish**.

Alternatively, you can create a Project Nature Working Set:

![http://javadude.googlecode.com/svn/wiki/images/naturews.gif](http://javadude.googlecode.com/svn/wiki/images/naturews.gif)

Name the working set, select the nature(s) you would like to include, and press **Finish**.

You can also select a Dynamic Maven Working set. Setup is similar to the regular expression working sets but the regular expressions apply to the group artifact ids of your m2eclipse projects.

If you like to use the **Top Level Elements->Working Sets**, switch back to it now.

![http://javadude.googlecode.com/svn/wiki/images/toplevel2.gif](http://javadude.googlecode.com/svn/wiki/images/toplevel2.gif)

Next, choose **Configure Working Set...** (If you're not using **Top Level Elements->Working Sets**, you can select which working set to use from the Select Working Set dialog where we created the new working sets.)

![http://javadude.googlecode.com/svn/wiki/images/configws.gif](http://javadude.googlecode.com/svn/wiki/images/configws.gif)

You'll see the Configure Working Sets dialog.

![http://javadude.googlecode.com/svn/wiki/images/packageexplorer.gif](http://javadude.googlecode.com/svn/wiki/images/packageexplorer.gif)

Check the working sets that you would like to appear in the Package Explorer and press **Ok**.

You should now see your working sets in the package explorer. The dynamic working sets will automatically manage their projects based on project name (for regular expression working sets) or project natures (for project nature working sets).

At my last job, I defined a few extra project natures (in a plugin) for organization and created separate instances of nature working sets for each. It really helps keep things organized!

Feel free to contact me at scott@javadude.com with questions or comments! Please let me know if you find this feature useful!

# License #
> Copyright (c) 2008 Scott Stanchfield.
> All rights reserved. This program and the accompanying materials
> are made available under the terms of the Eclipse Public License v1.0
> which accompanies this distribution, and is available at
> http://www.eclipse.org/legal/epl-v10.html