---
layout: post
title: "Organization and Revision Control"
date: 2012-12-13 23:37
comments: true
categories: 
- Development Environment
- Revision Control
---

For any complex project, organization is very important. Having a way to get yourself out of a mess can also come in handy. This time we discuss how to set up your code and stay organized, and how to control revisions of your work.  <!-- more -->

Your project should be organized in a way that facilitates its goals. For ExtrabiomesXL 4, the pertinent philosophies are:

  1. Mod code should be separate from protected Mojang code.
  2. Mod code should be separate from Minecraft Forge API code.
  3. Simplification is desirable.
  4. ExtrabiomesXL is a collaborative project. Its organization should not be an obstacle to others.
  5. Resources, e.g. artwork, non code config files, should be readily accessible and identifiable.
  6. All mod files should be located under one central (root) folder to make revision control easier. *More on this later.*

As it happens, it is fairly easy to satify all of these philosphies in our project organization. We'll get to that in a moment. For now, lets pause and discuss revision control...

## Revision Control

Writing software can be a daunting task. Many times you work on something for hours only to find out it doesn't work the way you thought it should. When this happens, you may find yourself scratching your head wondering how to get things back to the way that they were. Enter revision control...

Revision control, also known as version control or source code control, is the management of changes to collections of information. Specifically, revision control software provides an easy way for you to keep track of the changes that happen to your project as you work toward completing it.

To keep it simple, we won't get into every aspect of revision control or revision control software. It is enough to say that ExtrabiomesXL is a social open-source project. ``git`` is a stable modern full-featured *free* revision control system, contains many advanced features, and is supported by [github.com](http://github.com), a popular *free* social coding site. ``git`` fits the bill nicely.

Whatever system you use, you should put in the effort to know as much as you can about it. Knowledge of the revision control system can make every difference in a tight spot.

## Revision Control Procedures

Related to and just as important as project organization is revision control procedures. Strict adherance to revision control procedures directly affects project organization and can help all of your related goals. Likewise, failing to be disciplined with regard to revision control can work to counteract every positive effort in a project. Carefully choose a set of procedures that fits your goals while remaining something you can live with.

For ExtrabiomesXL 4, we want to use revision control to accomplish the following:

  - Maintain an official copy of the code, offering hotfixes for bugs requiring immediate attention.
  - Maintain a development copy of the code for current in-process work and beta testing.
  - Maintain separate copies of the code for in proces features that we can keep or throw away depending on the success of our work.

Above, each "copy of the code" is called a *branch*. The github repository for ExtrabiomesXL 4 will contain two permanent branches: master and devel. (Actually the github repository will contain four permanent branches. The other two have to do with hosting this web site.) We can also create temporary branches as needed for work in progress and merge them into one of the two permanent branches when work is successfuly completed. ``git`` allows us to freely switch between branches within the same folder structure of our project.

For those of you wanting to become familiar with ``git`` I hightly recommend the [reference manual](http://git-scm.com/documentation). For those wanting to set up revision control procedures, Benjamin Sandofsky wrote a great article, ["Understanding the Git Workflow"](http://sandofsky.com/blog/git-workflow.html).

## Return to Organization

Now that we understand more about the mechanisms involved in keeping our project organized, we can organize our project. As I mentioned, it turns out that there is a simple way to accomplish our goals for this project: Create a folder next to the "forge" folder we made [last time](/blog/2012/12/10/getting-started-with-minecraft-forge/).

![forge folder](/images/screens/Organization/1.png)

This solves almost all of our goals stated at the top, except for #5. We can satisfy #5 by navigating into our newest folder and creating a 'src' folder and any number of other folders to complete the organization of our project.

# Final Thoughts

I realize that this was a long dry article and reading some of the linked resources about revision control is the definition of tedium. But, trust me on this: *All of your efforts will be worth it because big projects (like your future popular Minecraft mod!) live and die by their organization and revision control systems.* **Make the investment.**

I realize I haven't covered the details about how to get your code into a revision control system. The reasons for that are two-fold:

  1. It is super easy.
  2. If I tell you how to do it and you don't read, it will spoil it for you.

Besides: you need to pick the system that fits *your* goals. There is no right choice and if I give you everything, I'm implying "one size fits all." It doesn't.
