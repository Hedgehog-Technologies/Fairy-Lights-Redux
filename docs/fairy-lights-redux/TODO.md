# Fairy Lights Redux migration plan

Step-by-step plan for porting the 1.20.1 Forge mod in `old/` to a Minecraft 1.21.8 multi-loader
project (Fabric, NeoForge, Forge). Work through it in order. Each step should be finished, tested and
committed before the next one.

## Where the project stands

- Done on this branch: the three loaders, the `buildSrc` plugins, `gradle.properties`, the two
  platform services (`IPlatformHelper`, `IConfigHelper`) and the 156 jingle JSON files in `common`.
  Only 17 Java files exist outside `old/`, and they are all scaffolding.
- Still to port: about 190 files in `old/` (103 server, 48 client, 35 util), plus 21 blockstates,
  about 30 item models, the entity models, textures, sounds, lang files, tags and recipes.
- Check first: `notes.md` describes a finished port (`MixinPlayer`, `FastenerHolder`,
  `DyedShapedRecipe`, `FLRecipeProvider`) that is not in this branch's tree. The branches
  `feat/multiloader-v1`, `feat/multiloader-v2` and `claude/1.21.8` exist. Run
  `git log --all --oneline` and `git diff feat/multiloader..feat/multiloader-v2 --stat` before step 1.
  If a finished port exists there, you may only need to review and merge it.

## Ground rules for every step

- Each step ends with `./gradlew build` passing and a commit in `type(scope): description` format.
- `common` must not import `net.fabricmc`, `net.minecraftforge` or `net.neoforged`. If you need a
  loader API, add a service to `IPlatformHelper`, implement it in all three loaders and add each
  `META-INF/services` file.
- Treat `old/` as read-only. Port behavior, not code verbatim.
- Run `runClient` on NeoForge and Fabric after each step that adds something visible. Say which
  loaders you did not test. Test Forge at milestones.
- Update `interactions.md` and `recipes.md` as you go, and record decisions in `notes.md`.

---

## [COMPLETED] Phase 0: Prepare

### [COMPLETED] Step 0: Confirm the baseline builds and launches

**Do:**
1. Run `./gradlew build`.
2. Run `:neoforge:runClient`, `:fabric:runClient` and `:forge:runClient`. Each should reach the title
   screen and log the "Loading FairyLightsRedux on ..." line from `CommonClass.init()`.
3. Check that `build/`, `.gradle/` and `run/` are ignored by git.

**Pitfalls:**
- The Forge 1.21.8 and JEI versions in `gradle.properties` may no longer resolve. Fix the
  dependency, don't skip the loader.
- `forge_version=58.1.11` is a very new Forge. Check its mixin and event APIs, because Forge differs
  from NeoForge more than before.

**Done when:** all three loaders launch to the main menu with your mod listed.

### [SKIPPED] Step 1: Inventory the old mod

**Do:** Write a checklist in `notes.md` that maps each old package to its destination, for example
`server/block` to `common/.../server/block`. Mark each package "port as is", "needs redesign" or
"drop".

**Check in the old code (verify, don't assume):**
- `capability/CapabilityHandler`: capabilities don't exist in the same form on Fabric.
- `net/*`: the old custom `Message` and `NetBuilder` system.
- `mixin/GameRendererMixin`: what it injects into.
- `integration/jei` and `integration/valkyrienwarfare`.
- `client/midi` and `client/command`: these are authoring tools.
- `data/DataGatherer` and `GenericRecipeBuilder`.

CLAUDE.md already decides some of these: replace the `GenericRecipe` engine with plain `CustomRecipe`
classes. Drop Valkyrien Warfare, `MidiJingler` and `/jingler` unless you want them.

**Done when:** every old package has a destination, and a "dropped on purpose" list exists.

---

## Phase 1: Registries and the simplest content

### [COMPLETED] Step 2: Registration pattern across loaders

**Do:**
1. Pick one way to register things in `common` and use it everywhere: registry-holder suppliers that
   each loader's entrypoint binds, or a platform service. Keep it simple, because CLAUDE.md warns
   against abstractions without a measurable payoff.
2. Port `Constants.id()` use and the `FL*` registry classes as empty shells: `FLBlocks`, `FLItems`,
   `FLBlockEntities`, `FLEntities`, `FLSounds`, `FLCraftingRecipes`.

**Pitfalls:**
- In 1.21.x, `Block.Properties` and `Item.Properties` need the registry key set (`setId`) before
  construction. Registering with a plain constructor throws at startup.
- Fabric registers immediately. NeoForge and Forge use deferred registers or events. Make sure your
  common code doesn't read a registry object before it exists.

**Done when:** a dummy item registers and shows up in the creative tab on all three loaders.
    - NOTE: I had to also include `FLCreativeTabs` to get to this done state.

### Step 3: Sounds, creative tab, config

**Do:**
1. Port `FLSounds`, `sounds.json` and the 3 sound files. Sound events are now `Holder`-based.
2. Port the creative tab. Fabric and NeoForge/Forge build tabs differently, so put the shared item
   list in `common`.
3. Implement `IConfigHelper` on each loader (the loader classes are stubs, so check what they do
   now). Options: christmas and halloween jingles, jingle amplitude, tutorial progress.

**Pitfalls:**
- Forge and NeoForge have a config system. Fabric doesn't, so decide a format. A properties file in
  `config/` works.
- `setTutorialProgress` is client-only and writes to disk. Don't call it from the server.

**Done when:** the config file is created on each loader and its values read back through `Services`.

---

## Phase 2: Core model (no rendering yet)

### Step 4: Pure logic: strings, jingles, math

**Do:** Port the pieces with no Minecraft dependencies first: `string/StringType(s)`,
`jingle/Jingle`, `JingleLibrary`, `JingleManager`, `JinglePlayer`, and the `util/` helpers you still
need. Skip `matrix/` and `OreDictUtils` if nothing uses them.

**Pitfalls:**
- The jingle JSON now lives in `common/src/main/resources/data/fairylightsredux/jingles`. The old
  loader read `data/fairylights/jingles`, so the namespace changes. Check the `christmas/` and
  `halloween/` subfolders are still found.
- Resource reload listeners are registered differently on every loader. Put the registration behind
  a service or an event hook, and keep the `Jingle` parsing itself in `common`.
- The old mod defined the `christmas_jingles` option but never read it. Decide whether to keep that.

**Done when:** a JSON jingle loads and parses on server start with the right count in the log.

### Step 5: Features and connection types

**Do:** Port `feature/*` (including `light/*`), `connection/*`, `Fastener`, `AbstractFastener`,
`BlockFastener`, `FenceFastener`, `PlayerFastener` and `accessor/*`. Port NBT serialization at the
same time.

**Pitfalls (the biggest API change):**
- NBT in 1.21.x: `CompoundTag` getters return `Optional` (since 1.21.5), and `ValueInput` /
  `ValueOutput` replace direct tag access in block entities and entities. Check the 1.21.8 mappings
  against what the old code calls (`getInt`, `getString`, `getCompound`).
- Fastener data used to hang off a capability. See step 7 for the replacement.
- Any custom registry (`ConnectionType`, `FeatureType`, `FastenerType`) is built differently per
  loader. If it's only used internally, a plain map in `common` is simpler than a real registry.

**Done when:** it compiles, and you can log a round trip of a connection's NBT even though you can't
place anything yet.

### Step 6: Blocks, block entities, fence fastener entity, items

**Do:**
1. Port `FastenerBlock`, `LightBlock`, `FastenerBlockEntity`, `LightBlockEntity`.
2. Port `FenceFastenerEntity`.
3. Port the items: `ConnectionItem` and its subclasses, `LightItem`, `PennantItem`, `ColorLightItem`,
   `DyeableItem`.
4. Add the blockstates, block models and item models. They are shared, so they go in
   `common/src/main/resources/assets/fairylightsredux`.

**Pitfalls:**
- Items: 1.21.4+ uses item model definitions in `assets/<ns>/items/*.json`. The old `models/item`
  files alone won't display. Dyeable color is now a data component (`DataComponents.DYED_COLOR` or
  your own) instead of NBT. Decide this before writing recipes in step 12.
- Entities: `SynchedEntityData`, constructors and spawn packets changed. A knot entity on a fence
  needs correct spawn behavior.
- Block entities: `saveAdditional` / `load` became `ValueOutput` / `ValueInput`, and `getUpdateTag`
  now takes a registry provider.
- Keep registry ids stable and in the `fairylightsredux` namespace. The old ids were `fairylights:*`.
- The old `LightBlock` models rely on emission order (see "Porting from `old/`" in CLAUDE.md). Port
  them first, then fix visuals in step 10.

**Done when:** you can get items from the creative tab and place a fastener block and a fence
fastener.

### Step 7: Replace the player capability

**Do:** Work out how to attach "what the player is carrying" (`PlayerFastener`, the in-progress
connection) without a Forge capability. First check whether Fabric attachments or NeoForge data
attachments suffice. They differ per loader, so a mixin on `Player` that exposes a plain field in
`common` is the cross-loader option.

**Pitfalls:**
- Register mixin classes in `*.mixins.json`. Test that the mixin actually applies on all three
  loaders (watch for "mixin not applied" in the log), especially Forge.
- Decide whether the data must survive death and respawn, and make sure it syncs to tracking clients.

**Done when:** a player can start carrying a connection, and switching away from the item drops it.

---

## Phase 3: Networking and interaction

### Step 8: Networking

**Do:**
1. Replace the old `Message` / `NetBuilder` system with `CustomPacketPayload` records and
   `StreamCodec`s in `common`.
2. Register the payloads per loader, then use the existing `IPlatformHelper.sendToPlayer`,
   `sendToPlayersTrackingEntityAndSelf` and `sendToServer`.
3. Port these packets: `JingleMessage`, `OpenEditLetteredConnectionScreenMessage`,
   `UpdateEntityFastenerMessage`, `EditLetteredConnectionMessage`, `InteractionConnectionMessage`,
   `ConnectionMessage`.

**Pitfalls:**
- Payload registration is per loader (Fabric `PayloadTypeRegistry`, NeoForge/Forge registrar
  events). Keep the handler itself in `common`.
- Handlers must run on the main thread. Server-bound packets need validation (distance, loaded
  chunk, valid connection), because that is a real system boundary.
- Clientbound handlers touch client classes. Keep them in a client-only class so a dedicated server
  doesn't crash.

**Done when:** a packet round trip works on each loader, tested with `runClient` plus `runServer`.

### Step 9: Interactions and redstone

**Do:** Port the click/use logic (hanging, finishing, length limits, snapping, leads, fences),
redstone-triggered jingles, comparator output and the note-block behavior. Use `interactions.md`
line by line as the checklist.

**Pitfalls:**
- Right-click and block events differ per loader. Prefer overriding on `Item` / `Block`
  (`useOn`, `useItemOn`) over loader events. Where a loader hook is unavoidable (leads on fences,
  note blocks), use a mixin or a loader event and keep it minimal.
- `useOn` and `useItemOn` were split in 1.20.5+, so check the `InteractionResult` return types.
- Neighbor-change and redstone signal methods got new signatures. Check `neighborChanged`.

**Done when:** you can hang lights between two blocks, see them snap at 37 blocks, and trigger a
jingle with a lever.

---

## Phase 4: Client

### Step 10: Rendering

**Do:**
1. Port the renderers: `client/renderer/block/entity`, `client/renderer/entity` and the model
   classes in `client/model`.
2. Port the render types (`UnsortedRenderType` and friends) and the connection renderer. This is the
   biggest client job.
3. Handle `GameRendererMixin` if it is still needed.

**Pitfalls:**
- The rendering API changed a lot between 1.20.1 and 1.21.8 (render state objects such as
  `EntityRenderState`, `RenderType` / `RenderPipeline` changes, model baking). Plan on rewriting, not
  patching. Use vanilla's lead rendering as a reference.
- Custom model loading differs between loaders. Avoid loader-specific model APIs unless necessary.
- Re-check the old atlas file under `assets/minecraft/atlases` and the entity pennant textures,
  because the item model system changed.
- Check translucency and sort order against the light-rendering rule in CLAUDE.md.

**Done when:** lights, pennants, tinsel, vines and letters all render correctly on NeoForge and
Fabric, and on Forge too.

### Step 11: GUI, editor and tutorial

**Do:** Port the letter-text editor screen (`client/gui`, `client/gui/component`), the jingle
overlay and the hanging-lights tutorial (`client/tutorial`).

**Pitfalls:** The `Screen` and `GuiGraphics` APIs changed (widget rendering, key handling). The
tutorial hooks vanilla's tutorial system, which was reorganized, so check what it injects into.

**Done when:** placing blank letter bunting opens the editor, and saving text updates the connection.

---

## Phase 5: Data and release

### Step 12: Recipes

**Do:** Replace `GenericRecipe` with `CustomRecipe` classes in `server/item/crafting`. Register them
in `FLCraftingRecipes`. Generate JSON with `SpecialRecipeBuilder` in `FLRecipeProvider` using
`./gradlew :neoforge:runData`. Use `FLRecipeProvider.dyed()` for fixed-shape colored items. Update
`recipes.md`.

**Pitfalls:**
- The recipe book and recipe display changed in 1.21.2+ (`RecipeDisplay`, `placementInfo`). Custom
  recipes need a display or they won't show in the book.
- Datagen only runs from the NeoForge module here. Make sure the generated output is committed under
  the shared resources directory.

**Done when:** every recipe in the old mod is craftable in game, and `recipes.md` matches.

### Step 13: Tags, loot, advancements, lang

**Do:** Move the 5 item tag files. Use common `c:` tags where possible and add Forge-only copies as
needed. Add loot tables (a fastener has no item, so it needs none). Add the lang files, and keep the
18 other locales.

**Pitfalls:** Loader tag conventions differ (`c:`, `forge:`). Missing keys fall back to English, so
check the new ones.

### Step 14: Optional integration and polish

**Do:** JEI (compile-only; there is no Forge JEI build for 1.21.8), the mod icon, and the metadata
files (`fabric.mod.json`, `neoforge.mods.toml`, `mods.toml`).

**Pitfalls:** The old `logo.png` is a wide banner. Fabric mod lists expect a square icon.

### Step 15: Final verification

**Do:**
1. Run `./gradlew build`.
2. Play through `interactions.md` on all three loaders, on a dedicated server, and with a second
   client.
3. Check logs for warnings.
4. Update the docs and tag a release.

**Done when:** a clean build produces three working jars, and you have noted which loaders and
scenarios you didn't test.

---

## Suggested order of risk

The riskiest steps are 5 to 7 (NBT and player data), 10 (rendering) and 12 (recipes). Do a quick
spike on each early: get one fastener block rendering in step 6 before the full renderer, and test
one `CustomRecipe` before porting the whole set.
