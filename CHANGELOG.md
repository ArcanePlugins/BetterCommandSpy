# BCS Changelogs

(⚠) Some versions may not have changelogs recorded in this file. Note that all changelogs are available on the Updates
tab on BetterCommandSpy's SpigotMC resource page.

(⬇) Sorted in descending order.

***

## v2.0.0 b23

### Notable Changes

* **Completely re-programmed the plugin from the ground up**. This took a while! Far better code now :) (@lokka30)
* Now with **fully** customisable, **multi-line** messages in `messages.yml`! (@lokka30)
* All messages now have **hex color code support** (this feature is only accessible to 1.16+ servers, and not on
  CraftBukkit servers)

### Other Changes

* Command spy now automatically disables itself when it detects a user no longer has permission to use it: improved
  server security. (@lokka30)
* Added permission `bettercommandspy.*` (@lokka30)
* Using MicroLib's `YamlConfigFile` class to manage config files (@lokka30)
* Separated the large command class into separate subcommand classes to make it easier to read and edit.
* Updated maven dependencies to latest version (@lokka30)
  * MicroLib.
    * All console logs now have color code support for all server implementations. (@stumper66)
  * bStats.
  * SpigotMC.