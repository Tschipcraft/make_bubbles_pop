- **Added native NeoForge support** 🦊 (closes [#19](https://github.com/Tschipcraft/make_bubbles_pop/issues/19))
  - Switched to Stonecutter in favor of a combined codebase and native NeoForge support
  - Switched from using MidnightLib to a custom Gson config on Fabric and native config on NeoForge and Forge.
    If installed, [Yet Another Config Library](https://modrinth.com/project/1eAoo2KR) or [Cloth Config](https://modrinth.com/project/9s6osm5g) in older versions will be used as the in-game config screen provider.
- **Added support for Minecraft 26.2** ♨️ **and 26.3** 🌲 (closes [#24](https://github.com/Tschipcraft/make_bubbles_pop/issues/24))
  - Added white bubble pop particles for sulfur bubbles spawned by geysers. 
    These are the first custom textures in this mod. Resource packs may retexture them in `assets/make_bubbles_pop/textures/particle/`.
- Changed bubble pop particles to inherit their size from the bubble that spawned them (Thanks @Chailotl)
- Fixed bubble pop sounds saturating the sound engine cap, preventing other sounds to play (closes [#18](https://github.com/Tschipcraft/make_bubbles_pop/issues/18))
- Lowered mixin `defaultRequire` to 0 to avoid crashes even when mixins fail to apply
- Added South Korean and Ukrainian language support (Thanks @Merhaf and @Mykytius, closes [#21](https://github.com/Tschipcraft/make_bubbles_pop/issues/21), [#17](https://github.com/Tschipcraft/make_bubbles_pop/pull/17))

## Supported targets

| Loader   | Minecraft      |
|----------|----------------|
| Fabric   | 1.18 - 26.3    |
| NeoForge | 1.21 - 26.3    |
| Forge    | 1.18 - 1.20.4  |
