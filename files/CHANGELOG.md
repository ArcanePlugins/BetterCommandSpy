# BetterCommandSpy Changelog

### v1.0.0-ALPHA
* Initial Release

### v1.0.1-ALPHA
* Renamed CommandSpy > BetterCommandSpy
* Added bStats metrics

### v1.0.2-ALPHA
* Updated Metrics

### v1.0.3-ALPHA
* Fixed file name of jar
* Renamed permissions
* Added `/bettercommandspy` alias to `/commandspy`
* Created documentation on the [Wiki](https://github.com/lokka30/BetterCommandSpy/wiki/).

### v1.0.4-ALPHA
* Fixed issue 'Permission node 'bettercommandspy.bypass' in plugin description file for BetterCommandSpy v1.0.3-ALPHA is invalid'

### v1.0.5-ALPHA
* Fixed the issue 'Cannot translate null text' when a player has already got an enabled / disabled state.
* Save config when state is first set on join.

### v1.0.6-ALPHA
* Adjusted and fixed file versioning system
* First joins now save a default state of 'false' if configured as well

### v1.0.7-ALPHA
* File changes: `messages.yml`. Please update this file else you will get errors!
* Changed `command.on` to `command.toggle_on` to fix a weird bug (also `command.off` to `command.toggle_off`)
* Code file path fixes