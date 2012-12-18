---
layout: post
title: "Beginning Your Project"
date: 2012-12-17 19:16
comments: true
categories: 
- Development Environment
- Java
---

Minecraft was written in the [Java programming language](http://en.wikipedia.org/wiki/Java_(programming_language\)) so your mod is also going to be written in Java. This has certain implications for your work:  <!-- more -->

First, of course, you are going to have to know Java. The good news is that it is not difficult to gain a working knowledge of Java. The bad news is that Java is difficult to master. In addition to learning the Java programming language, I highly recommend reading [Effective Java](http://www.amazon.com/gp/product/0321356683/ref=as_li_ss_tl?ie=UTF8&camp=1789&creative=390957&creativeASIN=0321356683&linkCode=as2&tag=killentime-20) by Joshua Bloch.

Second, while it is possible to develop in Java using a text editor, I find an Integrated Development Environment (IDE) is an invaluable tool. I use [Eclipse](http://www.eclipse.org/downloads/moreinfo/java.php) because it is (1) *free*, (2) supported by Minecraft Coder Pack (MCP) and (3) contains very powerful features.

## Using Eclipse

Setting up your project in Eclipse is simple: Open the MCP Eclipse project (found at forge/mcp/eclipse) and then add the source and resource directories (created [last time](/blog/2012/12/13/organization-and-revision-control/)) to the buildpath. (I won't tell you how to do it...you've got to get familiar with Eclipse!)

## Final Thoughts

If everything is set up properly, you will be able to play minecraft from the Eclipse 'Run' menu.

This will be the last of the basic "How-To" style posts. Beginning next time, we will be discussing the specifics of ExtrabiomesXL 4 design.