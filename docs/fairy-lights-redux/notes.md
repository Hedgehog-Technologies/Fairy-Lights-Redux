# Fairy Lights Redux porting notes

Decisions, assumptions and caveats from porting the 1.20.1 mod in `old/` to 1.21.8. Player-facing
behavior is in interactions.md and recipes.md; this file is for why things are the way they are.

The port reached parity with the old mod on 2026-10-13, and the follow-up gaps (wall and ceiling
stems, ingredient tags, mod icon, middle-click picking) have since been closed.

## Dropped on purpose

- The `/jingler` MIDI command and `MidiJingler`. They only converted MIDI files to jingle JSON for
  authoring songs. Its command strings aren't in the new lang file either.
- Valkyrien Warfare integration, `OreDictUtils` and the `matrix/` utilities.
- The Forge-only addon events (`CollectFastenersEvent`, `CreateBlockViewEvent`).
- The `GenericRecipe` engine. Special recipes are plain `CustomRecipe` classes instead.
- The fastener capability, replaced by `MixinPlayer` and `FastenerHolder`.
- The old entity pennant textures and blocks atlas. Pennants and letters render through item models.
- The old ladder item texture, which was never used.
- The template mixins.

## Differences from the old mod

- Registry ids all exist under the `fairylightsredux` namespace. Only the recipe serializers differ:
  the per-light special recipes became `DyedShapedRecipe` (637 recipes down to 52, and 632 unlock
  advancements down to 44).
- The old mod defined the `christmas_jingles` option but never read it. The new one does.
- Light swaying (`Light.startSwaying`) only ever ran from jingles in the old mod, so it was ported
  with jingles rather than with connection interaction.
- Glowstone dust in the hanging lights recipe makes the pattern's lights twinkle, and white dye for
  white string also works on existing hanging lights. Both are new features.
- Pick block on a connection doesn't work inside spawn protection for non-operators, unlike vanilla
  entity picking, because it shares the interaction packet's checks.
- Placed lights tick on the client (`LightBlockEntity.tickClient`) so twinkle, meteor and
  incandescent flicker animate on placed blocks.

## Assumptions

- A fastener has no item, so there is no fastener loot table. `FastenerBlockEntity.preRemoveSideEffects`
  drops the connection items.
- Recipe ids and unlock criteria were left unchanged when ingredients moved to tags, so the recipe
  book still unlocks on the vanilla items.
- White and red stained glass panes stay vanilla items, because no loader has a tag for one color
  of pane.
- Forge 1.21.8 only has `forge:bones` and `forge:feathers`, so the forge module ships `c:bones` and
  `c:feathers` pointing at them.

## Known caveats

- **JEI on Fabric:** JEI 24.2.0.6 never receives the server's recipes, so it shows no recipes for
  any mod there (only the item list). The recipe examples only show on NeoForge. There is no Forge
  JEI build after 1.21.1. JEI is `compileOnly` everywhere and only on the Fabric and NeoForge dev
  runtimes.
- **Mod icon:** `fairylightsredux.png` is the old 968x256 `logo.png` banner. NeoForge and Forge show
  logos that shape, but Fabric mod lists such as Mod Menu expect a square icon.
- **Translations:** the 18 other locales are carried over from the old mod wherever a key still
  exists (49 to 67 of 80 keys). Keys added since the old mod (the editor, jingle overlay, config and
  tooltips) only exist in en_us, and the rest fall back to English.
- **Fabric config:** there is no config screen. Options live in `config/fairylightsredux.properties`.
  NeoForge has a config screen; Forge doesn't.
- **Light rendering:** lights draw through `UnsortedRenderType` (`entity_translucent` without quad
  sorting, like the old mod's Forge unsorted type), because the models rely on emission order. Flat
  planes whose back face would sample an opaque texture should only emit their front face (see
  `JackOLanternLightModel`), and glow shells must not land on another shell's plane (see the skull's
  maxilla). Check both when porting new models.

## Architecture notes

- Connection interaction: `ConnectionHitHandler` runs from `MixinGameRenderer` after pick and swaps
  in a miss hit result so the block behind the cord isn't targeted. `MixinMinecraft` sends attacks,
  uses and pick block as payloads, and the server checks each one (the connection exists, spawn
  protection, range and reach).
- Pick block hands `Connection.getItemStack` to vanilla's `tryPickItem` through an invoker in
  `MixinServerGamePacketListenerImpl`.
- Pennants and letters render through the item model system, so no loader-specific model
  registration is needed. Each pennant's item definition selects a separate hanging model in the
  "none" display context, and each letter has an item definition under `items/letter/`.
- Jingles are loaded by `JingleManager`, a `SimpleJsonResourceReloadListener` registered on each
  loader. The note block bell hook is `NoteBlockEvent.Play` on NeoForge and Forge and
  `MixinNoteBlock` on Fabric.
- `IConfigHelper` is the platform service for config. `sendToServer` and `sendToPlayer` are platform
  service methods for payloads.
- `StyledString` (`util/styledstring`) is cut down to what the mod uses.

## Recipe pattern

Each special recipe is a plain `CustomRecipe` in `server/item/crafting`, registered in
`FLCraftingRecipes`, with its JSON generated by `SpecialRecipeBuilder` in `FLRecipeProvider`
(run `./gradlew :neoforge:runData`). A colored item with a fixed shape uses
`FLRecipeProvider.dyed()` instead, which makes one `DyedShapedRecipe`. `FLRecipeProvider.light()`
generates both the craft and redye recipes for any light from its shape.
