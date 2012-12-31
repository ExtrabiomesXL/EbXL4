---
layout: post
title: "Community Participation"
date: 2012-12-31 17:31
comments: true
categories: 
- Minecraft Community
- Design
---

Integration of the new terrain generator continues at a steady pace. I have decided to do something a bit unconventional.  <!-- more -->

I spent quite a bit of time working on adding support for Minecraft Forge terrain generation hooks and the Forge Mod Loader GameRegistry generateWorld to my fork of Terrain Control. My plan was to isolate and repackage the core TerrainControl mod. I had mixed feelings about this, but as I posted last time, Khoorn, the author had given me permission.

Khoorn, or Wickth as he is known on GitHub, saw what I had been doing to my copy of TerrainControl and he encouraged me to submit my changes to the TerrainControl project itself. His encouragement reminded me why I love opensource software, and ringing in my ears was the philosophy MisterFiber and I adopted months ago with regard to ExtrabiomesXL: "Be compatible with as many mods as possible." So, I spent a bit more time reworking my changes. (The TerrainControl core must be compatible with craftbukkit and thus contains no references to vanilla minecraft objects.) I submitted my changes, which were almost immediately included in the TerrainControl project. Since then, rutgerkok, a co-contributor to the TerrainControl project has optimized the code I submitted as part of an expansion of TerrainControl's own event system. This reminded me of a cool property of good opensource projects: egoless development. Khoorn invited me to be a part. Rutgerkok embraced my work and optimized it. I recognized rutgerkok's skill and superior knowledge of TerrainControl. The point is, this only worked because each of us focused on making a good product by doing our best work to that end and not allowing our egos to take over. It is a beautiful thing.

## The Impact on ExtrabiomesXL

ExtrabiomesXL will ship with the TerrainControl core *included*. If you also install the TerrainControl mod, the two will share code. This works because of how well TerrainControl has been designed and it means that players will be able to have the best of both worlds, no pun intended.

ExtrabiomesXL will tweak the settings of TerrainControl to provide its own content and biomes.

This also changes our [design goals](/blog/2012/12/21/design-goals/):

* TerrainControl accomplishes the first four goals on the list, *out of the box*.
* We will no longer have a need for a "grove layer" as TerrainControl can already accomplish the grove layer's purpose pretty well.
* We will provide a GUI at world creation for simple tweaking.
* We will provide the ability to load "world packs", kind of like texture packs, so that users can choose a world "style" on the world creation screen.

That last goal brings me to a request--a mission for you, if you will: This last goal opens up a potential niche for budding world creators. Those who know how TerrainControl configurations files work will be able to create world styles for other players. If any of you suspect you might like to fill this market, I recommend learning TerrainControl [here](https://github.com/Wickth/TerrainControl/wiki).

I had all of this on my mind a few days ago when a team member sent me a cool idea for a new type of world he wanted to see. Being able to say "The code in version four will *already* do that." gave me a great feeling. This has the potential to be very big.