---
layout: post
title: "Getting Started with Minecraft Forge"
date: 2012-12-10 22:50
comments: true
categories: 
- Minecraft Forge
- Development Environment
---

Setting up the mod development environment used to be a complicated set of steps, but with recent developments in Minecraft Forge, it has become very easy. <!-- more -->

Here are the steps:

1. Create a folder for your project.
2. Go to [http://files.minecraftforge.net/](http://files.minecraftforge.net/) and download the latest Forge source (the "src" link).
3. Open the .zip file you downloaded and put the *forge* directory into the project folder you created, like so:

    ![forge folder](/images/screens/GettingStarted/1.png)

4. Enter the forge folder.
5. Run `install.cmd` in Windows or `install.sh` in Linux/MacOS

At this point, Minecraft Forge will take over, downloading everything it needs to decompile Minecraft and set up a development environment.

**IMPORTANT UPDATE:** There is currently (Forge build #436) a bug in the Forge install script that causes the eclipse project not to be set up properly. The Forge team knows of the bug and is fixing it. For now this install procedure will not work. Stay tuned here for updates.
