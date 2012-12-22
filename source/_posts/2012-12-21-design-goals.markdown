---
layout: post
title: "Design Goals"
date: 2012-12-21 21:56
comments: true
categories: 
- ExtrabiomesXL
- Design
---
Lots of people have asked what is coming in the next version. This time, we get into the design goals for ExtrabiomesXL 4. <!-- more -->

When I begin a software project, I like to layout my goals. I list everything I want to do, understanding that it *will change* because of the [Project Management Triangle](http://en.wikipedia.org/wiki/Project_management_triangle). The PMT summed up is "Pick two: Fast, Good, Cheap." Because of the interplay of these three attributes, project design is constant compromise.

Here is our list for the next version of ExtrabiomesXL:

- Rather than override vanilla terrain generation, we need to have a selectable world type, in the same way that players can select "Large Biome" or "Flat" worlds.
- Expand terrain gen into the full 256 block height along the y-axis. Currently vanilla generation takes place in the lower 128 blocks.
- Change the use of biomes: Currently vanilla biomes determine a lot of things, including climate, terrain altitude and flora (plants and trees). This is fine for vanilla, with only a few types of flora, but we are going to expand the number of floral species. Extrabiomes should not determine flora.
- Add extremely rare biomes (e.g. enchanted groves, wasteland, extreme mountains, glaciers).
- Increase the number of floral species. Nightshade_Proleath bequeathed the [Forgotten Nature](http://www.minecraftforum.net/topic/1519278-) mod to us, which includes many, many floral species.
- Create a grove layer, which will overlay the biome layer in terrain generation to shape forests so that they transcend the boundaries of biomes. This will allow experience such as finding a jungle in the lowlands and watching the plants and trees change as you move through different climates and altitudes. Also, within forests, scarce groves with mostly one type of tree can be found. This improves the exploration experience.
- Add blocks to support the above. All blocks have a use which is added via technology tree or integration with other mods. This provides the motivation for exploration.
- Add a simple API, that does not require inclusion in another mod or hard dependencies of any kind.
- Integrate with other mods, with priority given to [FTB](http://www.feed-the-beast.com/) mods, via published APIs, while avoiding inclusion of APIs and without hard dpendencies.
- Include more rock types and generate [stratum](http://en.wikipedia.org/wiki/Stratum).
- Improve textures.

This is our starting list. It is ambitious and long; however, the secret to good design is to shoot for the stars and accept the Sun.