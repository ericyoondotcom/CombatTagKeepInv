# CombatTagKeepInv

Basic plugin to allow players to keep their inventory when they die to natural causes, but drop their items when they are killed in PvP combat.

- Plugin will only work in worlds specified in `config.yml` that have the `keepInventory` gamerule set to false.
- Players with the permission `combattagkeepinv.exempt` will keep their inventories regardless of if they are combat-tagged. (Note: this permission must be explicitly set, operators will not automatically have it)

## Config settings
- `cooldownTime`: How long a player will be combat-tagged for when hit, in seconds. (int)
- `killCombatLoggers`: Set to `true` to kill combat-tagged players when they disconnect. (boolean)
- `worlds`: The names of the worlds in which the plugin will be active. (string[])
