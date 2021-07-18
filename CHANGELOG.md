# BCS Changelog

(In descending order)

## v2.0.0 b23

* Updated MicroLib dep to latest version
* Updated bStats dep to latest version

# Old Changelog

(In ascending order)

### v1.0.0-ALPHA b1

* Initial Release

### v1.0.1-ALPHA b3

* Renamed CommandSpy > BetterCommandSpy
* Added bStats metrics

### v1.0.2-ALPHA b5

* Updated Metrics

### v1.0.3-ALPHA b8
* Fixed file name of jar
* Renamed permissions
* Added `/bettercommandspy` alias to `/commandspy`
* Created documentation on the [Wiki](https://github.com/lokka30/BetterCommandSpy/wiki/).

### v1.0.4-ALPHA b9
* Fixed issue 'Permission node 'bettercommandspy.bypass' in plugin description file for BetterCommandSpy v1.0.3-ALPHA is invalid'

### v1.0.5-ALPHA b10
* Fixed the issue 'Cannot translate null text' when a player has already got an enabled / disabled state.
* Save config when state is first set on join.

### v1.0.6-ALPHA b11
* Adjusted and fixed file versioning system
* First joins now save a default state of 'false' if configured as well

### v1.0.7-ALPHA b14
* **[Important]** File changes: `messages.yml`. Please update this file else you will get errors!
* Changed `command.on` to `command.toggle_on` to fix a weird bug (also `command.off` to `command.toggle_off`)
* Code file path fixes
* Code permission fixes
* Skip sending 'by' message if target = sender
* Added build number (e.g. 'b14') to the version.
* Removed extra `/` in command alert message.

### v1.1.0
* **IMPORTANT:** `settings.yml` has been updated. You must update this file, else you will experience errors.
* **IMPORTANT:** This update has not been tested. Please test it before deploying it on a production server.
* Added: ignorable commands section in the settings file with whitelistable and blacklistable commands!
* Improved: a decent amount of code has been moved around to make the code cleaner :)
* Improved: shaded in my MicroLib library which made some minor improvements.

### v1.1.1
* **No configuration changes.**
* Removed hidden debug feature that I forgot to remove a while ago.
* Updated deps. (Spigot, MicroLib)

### v1.1.2
* Fixed outdated README.md
* Fixed alert messages not having a coloured prefix (thanks lolsloths for reporting this!)
* Added `%displayname%` placeholder for alert messages