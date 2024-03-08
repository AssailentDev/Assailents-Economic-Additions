# Economic Additions
## A Highly Customisable Economy Plugin.
### Version v0.0.2

#### Commands:
 
* **/economy** \
**/economy <bal | give | take | set> \<player> \[\<amount>]** \
Requires the permission "EconomicAdditions.economyadmin", \
/economy bal \<player> outputs the balance of a player. \
/economy give \<player> \<amount> adds the given amount to a players balance \
/economy take \<player> \<amount> removes the given amount from a players balance \
/economy set \<player> \<amount> sets the players balance to the given amount
* **/bal** \
**/bal [player]** \
Aliases: "balance"\
Requires the permission "EconomicAdditions.balance" \
/bal outputs the balance of yourself. \
Requires the permission "EconomicAdditions.balance.others" \
/bal \[player] outputs the balance of the given player.
* **/pay** \
**/pay \<player> \<amount>** \
Requires the permission "EconomicAdditions.pay" \
/pay \<player> \<amount> pays the given player the amount specified from the users balance.
* **/toggleactionbar** \
**/toggleactionbar** \
Aliases: "actionbar", "ab" \
Requires the permission "EconomicAdditions.toggleactionbar" \
/toggleactionbar turns the action bar on / off.

#### Configuration

##### Config.yml
```yml
prefix: "<gray>[<gold>Economic Additions<gray>] " # Sets the prefix of messages sent by the plugin.
```

##### Lang.yml
```yaml
economy:
  currency-singular: "Sheep" # What does the currency show up as when its singular e.g. "1 Sheep"
  currency-plural: "Sheeps" # What does the currency show up as when its plural e.g. "120 Sheeps"
  starting-money: 1500 # How much money do players start with?
  error-color: "<#EB7272>" # What colour (Adventure Library Format) should error messages appear as.
  success-color: "<#A6F8C6>" # What colour (Adventure Library Format) should successful messages appear as.
  sender-not-a-player: "Only players can use this command!" # When the command sender is not a player!
  target-hasnt-played-before: "The player %parse% has never played on this server before!" # When the target has not played before!
  balance: "%parse%'s balance: %currency%" # %parse% = players name, %currency% = their balance. e.g. "1 Sheep"
  non-valid-number: "The amount must be a valid decimal number. Got: '%parse%'." # Non valid number error message
  took-money: "Took %currency% from %parse%'s account." # /economy take <player> <amount> %currency% = amount e.g. "10 Sheeps" %parse% = players name
  gave-money: "Gave %currency% to %parse%'s account." # /economy give <player> <amount> %currency% = amount e.g. "10 Sheeps" %parse% = players name
  set-money: "Set %parse%'s account to %currency%" # /economy set <player> <amount> %currency% = amount e.g. "10 Sheeps" %parse% = players name
  not-enough-money: "You don't have enough money to pay %parse%" # /pay <player> Error message when player doesnt have enough money to pay target.
  paid-player: "You paid %parse% %currency%" # /pay <player> %currency% = amount e.g. "10 Sheeps" %parse% = players name
  recieved-money: "You were paid %currency% from %parse%" # When you recieve money from a /pay <player> run command by another player %currency% = amount e.g. "10 Sheeps" %parse% = players name
  no-permission: "You don't have permission for this command!" # When the player has no permission!
actionbar:
  enabled: true # Whether or not the action bar can be used
  update: 2 # How often in seconds should it refresh (Recommended 2)
  text: "<gray>%vault_eco_balance% Sheeps" # What should the action bar display, (Colours in Adventure Library Format) (%placeholders% in PlaceholderAPI format, May require /papi ecloud download vault)
  toggle-on-text: "<green>Action bar toggled ON!" # Chat message when /toggleactionbar turns the action bar on.
  toggle-off-text: "<red>Action bar toggled off!" # Chat message when /toggleactionbar turns the action bar off.
  sender-not-a-player: "Only players can use this command!" # Runs when the sender of the command is not a player!
```