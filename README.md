# Screenshot Settings

ScreenshotSettings is a mod for Fabric (and hopefully Quilt, but I haven't tested it yet) that allows you to configure
various things about in-game screenshots.

"Various Things" includes choosing where screenshots are saved to on-disk, as well as how you'd like them to be named.
Relevant information to a screenshot can be optionally embedded into the filename or metadata, such as player position,
rotation, active texture or shader packs, the name of the world or server the screenshot was taken on, and if on
singleplayer, the world seed.

This should allow for convenient sorting/searching through screenshots by relevant metadata, and is also nice just for
making your screenshots more accessible. (Seriously, I hate having to go into my AppData folder when I'm playing on
Windows. It's terribly annoying.)

## This mod is incomplete.

In fact, it's not even in alpha. There are currently no releases because almost none of the features planned are
implemented yet.
Here's a convenient checklist of what's been done.

- [x] Mixin to ScreenshotRecorder to gain control of where screenshots are saved and how they are named.
- [x] Implement an Options screen, likely through ClothConfigAPI or a similar library
- [ ] File Explorer dialogue for choosing the new default screenshots directory
- [x] Implement system for manipulating PNG metadata
- [x] Getters for player position, rotation
- [x] Getters for world/server name, game version
- [x] Getter for active texture pack
- [ ] Getter for active shader pack (Iris integration maybe?)
- [x] Editor for filename schema
- [ ] Apply filename schema with templating system