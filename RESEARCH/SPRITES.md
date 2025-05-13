# Research Report
## 8-bit Sprite Rotation/Rasterization
### Summary of Work
I researched what techniques the team can impliment to rotate 8-bit sprites on a screen. Two techniques are prominant:

1) (Fast) RotSprite: Blowing up sprites and rotating them on high definition displays, maintaining the 8-bit look.
2) Rasterization: Using rasterization over sprites to create an 8-bit resolution


### Motivation
Since we are doing grandparent games, we needed to come up with a consensus of what display technique we are going to use, as it affects the art and backend development. 
### Time Spent
I spent 15 minutes looking around for how 8-bit sprites are displayed. Most of this time is researching how the game `Celeste` does their 8-bit display, but found not too much on that. 

I spent 5 minutes looking for software to create sprites that could also have animation for future development. 

I spent 10 minutes looking deeper on the on techniques above through wikis, etc. 

I spent 10 minutes creating a brush and canvas profile on my iPad through `Procreate` so I am able to create the 8-bit sprites to be used in the game. 
### Results
<!--Explain what you learned/produced/etc. This section should explain the
important things you learned so that it can serve as an easy reference for yourself
and others who could benefit from reviewing this topic. Include your sources as
footnotes. Make sure you include the footnotes where appropriate e.g [^1]-->

I found that using RotSprite[^1] would be less likely to be buggy compared to Rasterization, where small sprites' details (like bullets) could travel on a diagonal of the rasterized display, causing them to disappear. 

It does have a tradeoff, where it isn't technically 8-bit display because it allows sprites to tilt on any degree. 

The reason why we are considering RotSprite over the second is because our spaceship needs to be able to turn at a precise angle[^2]. It is easier to code this way and may result in a similar final product compared to the true 8-bit rasterization method. 

I found software to create sprites called `Aseprite`[^1], but I am also now able to create sprites through `Procreate`[^4]. 

Future meetings will determine if we will use RotSprite, Fast RotSprite, or similar methods. 
### Sources
<!--list your sources and link them to a footnote with the source url-->
- Aesprite[^1]
- RotSprite[^2]
- Pixel Scaling Wiki[^3]
- Procreate[^4]

[^1]: https://www.aseprite.org/
[^2]: https://gamedev.stackexchange.com/questions/135091/how-can-i-rotate-pixel-art-sprites-without-the-aesthetics-getting-ruined
[^3]: (https://en.wikipedia.org/wiki/Pixel-art_scaling_algorithms#RotSprite)
[^4]: https://www.softwarehow.com/make-pixel-art-procreate/
