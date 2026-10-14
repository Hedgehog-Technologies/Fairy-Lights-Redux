# Fairy Lights Redux interactions

How every object in the mod behaves when players, redstone and other blocks interact with it, and the
caveats that come with each one, as currently ported. Update this file whenever an interaction is
added or changed. Recipes are in recipes.md.

## Connection items

Hanging lights, pennant bunting, tinsel garland, vine garland and letter bunting are all connection
items, and they're placed the same way.

### Hanging a connection

1. Use the item on a block, a fence or an existing fastener. This is the start of the connection.
2. The connection now runs from that fastener to your hand. The item isn't used up yet.
3. Use the item on a second block, fence or fastener to finish. The item is used up here, and the
   connection now runs from the first fastener to the second.

Where a fastener can go:

- **On a block face:** clicking a face places a fastener block on it, if the space is free and the
  face can hold it: a full (sturdy) face, any face of leaves, or the top of a wall.
- **On a fence:** clicking any block in the fences tag hangs a fence fastener (an entity) on it. One
  fastener per fence block, and not on a fence that already has a leash knot.
- **On an existing fastener:** the new connection shares it. A fastener can hold any number of
  connections.

Caveats:

- You can't finish a connection on the fastener it started from, or on one that's already connected
  to the starting fastener.
- While you carry a connection, you can't start a different kind with another connection item.
- Switching away from the item you're carrying with (so neither hand holds a matching stack) drops
  the connection. Nothing is lost, since the item wasn't used up.
- Connections you're carrying can't be targeted or interacted with.
- Letter bunting with no text opens the text editor as soon as it's finished.
- Two fence fasteners stacked directly above each other get a spiral instead of a hanging curve.

### Length

Connections can be up to 32 blocks long. Past 27 blocks a stretching sound plays, getting higher
the further you pull. Past 32 a player carrying the connection is pulled back towards the start. At
37 the connection snaps and is removed.

### Leads

A leashed mob can't be tied to a fence that has a fence fastener, and holding a connection item in
either hand stops leads tying mobs to fences, so the click goes to the connection item.

## Fasteners

### Block fastener

- There's no fastener item. Fastener blocks are only placed by connection items.
- A fastener block removes itself once it has no connections.
- Breaking it (or the block it's on) drops every connection item attached to it, in either
  direction.
- It falls off and drops its connections if the block it's attached to can no longer hold it.

### Fence fastener

- A small knot entity on the fence. Use a connection item on it to start or finish a connection.
- Hitting it breaks it and drops its connections. It also breaks if the fence is removed, and when
  its last connection goes.
- Explosions don't affect it.

### Redstone

A block fastener that goes from unpowered to powered plays a jingle on the hanging lights that end
at it (see Jingles). A comparator next to a fastener outputs how far through the current jingle the
lights are, 0 to 15. Fence fasteners don't respond to redstone.

### Note blocks

A note block with a fastener hanging from its underside (a fastener facing down) rings the jingle
bell at the note block's pitch instead of playing its instrument. The note particle still shows.

## Using a connection

Look at a connection's cord or one of its decorations to target it (it's outlined). The cord's first
and last segments aren't targetable, so the fasteners at each end stay clickable. With the High
Contrast Block Outline accessibility option on, the outline is cyan on black, like block outlines.

### Every connection type

- **Attack:** disconnects it and drops the item where you hit it.
- **Use with string** (anything in `c:strings`)**:** loosens it a step. **Use with a stick:** tightens
  it a step. Slack goes from 0 (taut) to 3, and the default is 1.
- **Use with a different connection item:** swaps the connection for one made from the held item.
  The old item goes into your inventory and the slack is kept.

- **Pick block (middle-click):** gives you the connection's item, with its pattern, colour or text.
  In survival it selects a matching stack from your inventory if you have one; in creative it adds
  one if you don't, the same as picking an entity.

Interactions only work where you could edit blocks (spawn protection and adventure mode apply) and
within reach of the target. In creative mode, interactions don't use up the held item. Pick block
has the same spawn protection and reach checks.

### Hanging lights

- **Use a light on a light:** swaps that light, and every light in the pattern at the same
  position, for the held light. The old light goes into your inventory.
- **Use anything else:** toggles the whole string on or off. This includes using a light on the
  cord rather than a light.

While on, a string places invisible light blocks at its lights so they light up the area. They're
placed when the string is hung or changed and re-checked one light at a time about every 20 seconds,
and only go into empty air that isn't already at full brightness. They're removed when the string
is turned off, moved or taken down.

Lights are off while either end is moving (being carried). Oil and candle lanterns give off flame
and smoke particles.

### Pennant bunting

- **Use a pennant on a pennant:** swaps it, like lights on hanging lights.
- **Sneak-use:** opens the text editor.

### Letter bunting

- **Sneak-use:** opens the text editor.

### Tinsel and vine garland

Nothing beyond the interactions every connection has. A vine garland shows a red bow on each
fastener it's attached to: on a wall fastener the bow faces out from the wall, and on a fence it
goes on an open side of a straight run. Floor and ceiling fasteners, and fence posts with no arms,
get no bow.

## Text on bunting

The text editor sets the text on pennant bunting and letter bunting.

| | Pennant bunting | Letter bunting |
| --- | --- | --- |
| Characters | Anything except combining marks | 0-9, A-Z, &, ! and ? (typed letters become capitals and lose accents) |
| Styling | Colour, bold, italic, underline, strikethrough | Colour only |
| Length | At most one character per pennant | Must fit along the cord (I and ! are narrower) |

Editor keys:

- **Ctrl+B, I, U, S:** toggle bold, italic, underline, strikethrough.
- **Ctrl+\\:** clears styling from the selection, or from what you type next.
- **Ctrl+V:** pastes, keeping any § formatting codes. **Ctrl+Shift+V:** pastes as plain text.
- Double-click selects a word, triple-click selects everything, and dragging a selection moves it.
- **Enter:** Done. **Esc:** cancel.

Done is greyed out while the text is unchanged or doesn't fit the rules above. The server checks
the text again and ignores it if it doesn't fit.

Pennant text is centred along the bunting and drawn on both faces, reversed on the back so it reads
the right way from behind. Changing the pennant pattern by crafting clears the text.

## Placed lights

Every light item can also be placed as a block.

- **Placing:** on a wall, the floor or a ceiling, depending on where you look. Walls need a full
  face. Floors and ceilings need at least a 2×2 pixel area of support in the middle, so fences and
  walls work. Any face of leaves works.
- **Use with an empty hand:** toggles it on or off. Lit lights give off light level 15.
- **Use a dye:** recolours dyeable lights. Modded dyes work if they're in the `c:dyes/<color>`
  tags. It does nothing on undyed lights (oil lantern, candle lantern, incandescent light) or if the
  light is already that colour.
- **Breaking and pick block:** both give back the exact item, including its colour and modifiers.
- Twinkling, meteor and incandescent flicker animate on placed lights too, and incandescent lights
  fade on and off when toggled, like on hanging lights.

Caveats:

- Placed lights don't play jingles.
- Dyeing a colour-changing light sets its underlying colour, but it keeps changing colour.

## Jingles

A redstone pulse on a block fastener plays a song on the hanging lights that end at that fastener.
That's the second fastener you clicked when hanging the string, so powering the starting fastener
does nothing. Only one string per fastener plays, and only if it isn't already playing.

- **Song choice:** a random song whose notes fit on the string. Each light is one note, so a string
  needs as many lights as the song's range (most songs need several; any song fits on 25 or more).
  Wider songs are picked more often.
- **Holidays:** from December 24 to 26 the Christmas songs play (unless christmas_jingles is off),
  and on October 31 the Halloween songs play.
- **Playing:** each note rings its light's bell, shows a note particle and makes the light sway. A
  few songs add hearts or lava particles.
- **Overlay:** looking at lights that are playing shows the song and artist in the top-left corner.
- **Comparator:** see Redstone under Fasteners.

The songs are data in data/fairylightsredux/jingles: songs in christmas/ and halloween/ are those
libraries, and the rest are the random library. Datapacks can add songs in the same format.

## Items

- Empty hanging lights (no pattern) show red and green lights blinking at Christmas, and orange and
  purple at Halloween.
- Colour-changing lights cycle through their colours in the inventory too.
- Tooltips show a light's modifiers (Twinkles, and the colour list), a hanging lights or pennant
  bunting pattern, and bunting text.

## JEI

With JEI installed:

- Each colour of a light, pennant or tinsel is its own JEI entry (Fabric and NeoForge).
- On NeoForge, the special recipes (hanging lights, pennant bunting, changing a pattern, twinkling,
  colour changing, dyeing and copying a colour) show cycling examples. Looking up an item shows the
  example that uses or makes it.
- Hovering a slot gives the old hints, such as adding white dye for white string.

Caveats:

- JEI shows no recipes at all on Fabric, for any mod, and says "JEI is missing recipes" on joining
  a world. Since 1.21.2 the client only gets recipes from the server, and JEI 24.2.0.6 (the last
  Fabric build for 1.21.8) never receives them on Fabric; its NeoForge build does.
- Forge has no JEI for 1.21.8.

## Tutorial

The first time any light is in your inventory, a toast suggests crafting hanging lights. It stays
until you have hanging lights in your inventory or have crafted some, and goes away if you no longer
have any lights. It's shown once per player profile, not per world: its progress is the tutorial
progress option in the client config, and setting that back to none shows it again.

## Config

| Option | Default | What it does |
| --- | --- | --- |
| christmas_jingles | true | Whether jingles switch to the Christmas songs from December 24 to 26 |
| jingles_amplitude | 40 | How far away jingle bells can be heard, in blocks (at least 1) |
| tutorial progress | none | Client only. How far through the tutorial this player is: none, hanging_lights or complete |

- **NeoForge:** config/fairylightsredux-common.toml, plus config/fairylightsredux-client.toml for
  the tutorial (as `[tutorial] progress`). Both are editable from the mod list's config screen.
- **Forge:** the same two files. There's no in-game screen.
- **Fabric:** everything in config/fairylightsredux.properties (the tutorial as
  `tutorial_progress`), written with the defaults on first launch. Invalid values fall back to the
  default with a warning in the log, and are rewritten as the default the next time the tutorial
  progress saves.

jingles_amplitude is read by each client, so on a server every player uses their own value.
