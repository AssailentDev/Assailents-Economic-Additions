# Assailents Economic Additions
## Introduction
Assailents Economic Additions is my solution to the current struggle to find a good bloat-free and customizable economy plugin. After a while of running/developing servers (Homieworld and Aeros before they shut down and while they were under original owners) and struggling to find good Economy Plugins I decided to make my own.
## Requirements:
* Vault (Even thought it says last test version is 1.17 it works for 1.20.4): https://www.spigotmc.org/resources/vault.34315/
* PlaceholderAPI
https://www.spigotmc.org/resources/placeholderapi.6245/

## Commands & Permissions
As the Introduction says, with the goal to stay bloat free there are only a couple commands.
### Admin Commands
* /economy <bal | give | take | set> \<player> \<amount> \
Get the balance of a player, give the player money, take money from the player, or set the player's balance. \
Permission Required: `EconomicAdditions.economygui` \
Aliases: N/A

### User Commands
* /bal [player]  \
This returns the balance of a player. \
Permission Required: `EconomicAdditions.balance` and `EconomicAdditions.balance.others` \
Aliases: /balance

* /pay \<player> \<amount> \
This pays a player from you're balance. \
Permission Required: `EconomicAdditions.pay` \
Aliases: N/A

* /toggleactionbar \
This toggles the action bar above the players hotbar (See Custom Features) \
Permission Required: `EconomicAdditions.toggleactionbar` \
Aliases: /actionbar /ab

* /economygui \
Opens the economy gui (Allows for paying and balances, Balances are viewable no matter the permissions) \
Permission Required: `EconomicAdditions.economygui` \
Aliases: /econgui /econ /egui /money

* /stockdisplay \
This toggles the Stock display (See Custom Features) \
Permission Required: `EconomicAdditions.stockdisplay` \
Aliases: /toggledisplay /togglestock /toggletickdisplay /stock /display /tickdisplay

## Custom Features
### Mini Message support
Every message supports AdventureLib mini message formatting! Allowing for gradients, custom colours and etc...
Link to MiniMessage Formats: https://docs.advntr.dev/minimessage/format.html

### Action Bar
The action bar is a display that can be toggled in the lang.yml (See Configuration), By default it updates every 2 seconds and displays the balance of the player and the name of the currency above it.

### Stock display
This essentially is just a title that appears and ticks up from 80% of an amount of money recieved or taken up to the full %. Can be toggled in the lang.yml (See Configuration)

### Economy Gui
Just a basic UI that allows the player to Pay, View Balances, And other stats in the form of a UI!
It also has page support if you have more than 27 players online and everything is customisable!

## Configuration

In the configuration files pretty much two types of configuration that you need to know about; \
Normal Configuration, \
Items.

### Normal Configuration
This is pretty straight forward. Things like `starting-money: 1500` mean that players start with 1500. And doesn't require much thought. This is also where almost EVERY message in the plugin can be customised (Including translated or completely different messages.

### Items
Items are mainly defined within the `gui/economy:` section; and have a few key features! \
Here is an example item
```yml
TestItem:
  Material: "DIAMOND" # Grab materials from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html besides "PLAYERHEAD" more on that later.
  Amount: 1 # How many in the ItemStack
  Title: "Diamond Stick" # Name of the item
  Lore:
    - "This is the first line of lore!"
    - "This is the second!"
    - "You can have as many lines as you want!"
    - "I support MiniMessage and PlaceholderAPI format!"
 ```
 This item wouldn't show up as the plugin doesnt call for it. This is why you must NOT! Change the names of the variables in the lang.yml. \
 You can add CustomModelData to the items with just a simple line at the bottom \
 Example:
 ```yml
 TestItem:
   Material: "DIAMOND"
   Amount: 1
   Title: "Diamond Stick"
   Lore:
     - "Some Cool Lore"
     - "Yep!"
   CustomModelData: 1
   ```
   Custom model data is only useful if you know what you're doing!

### Full Config Files

#### Lang.yml
```yml
common:
  prefix: "<gray>[<gold>Economic Additions<gray>] "
  currency-singular: "Sheep"
  currency-plural: "Sheeps"
  starting-money: 1500
  error-color: "<#EB7272>"
  success-color: "<#A6F8C6>"
  non-valid-number: "The amount must be a valid decimal number. Got: "
  sender-not-a-player: "Only players can use this command!"
  target-hasnt-player-before: "The player %parse% has never played on this server before!"
  no-permission: "You don't have permission for this command!"
economy:
  took-money: "Took %currency% from %parse%'s account."
  gave-money: "Gave %currency% to %parse%'s account."
  set-money: "Set %parse%'s account to %currency%."
  balance: "%parse%'s balance: %currency%"
  paid-player: "You paid %parse% %currency%"
  recieved-money: "You were paid %currency% from %parse%"
  not-enough-money: "You don't have enough money for that!"
actionbar:
  enabled: true
  update: 2
  text: "<gray>%vault_eco_balance% Sheeps"
  toggle-on-text: "Action bar toggled ON!"
  toggle-off-text: "Action bar toggled OFF!"
stockdisplay:
  enabled: true
  min-amount: 2000
  negative:
    prefix: "<gray>[<red>-<gray>] "
    suffix: " <gray>Sheeps"
  positive:
    prefix: "<gray>[<green>+<gray>] "
    suffix: " <gray>Sheeps"
  toggle-on-text: "Stock Display toggled ON!"
  toggle-off-text: "Stock Display toggled OFF!"
gui:
  economy:
    messages:
      cancel-payment: "Cancelling Payment!"
    mainMenu:
      Title: "Economic Additions"
      payItem:
        Material: 'DIAMOND'
        Amount: 1
        Title: "<gold>Pay Players!"
        Lore:
          - "<gray>Click on this to open the pay player menu!"
          - "<gray>EconomicAdditions"
      balItem:
        Material: 'GOLD_INGOT'
        Amount: 2
        Title: "<gold>Balances!"
        Lore:
          - "<gray>Click on this to check the balances of other players!"
          - "<gray>EconomicAdditions"
      statsItem:
        Material: "PLAYERHEAD"
        Amount: 1
        Title: "<gold>Stats!"
        Lore:
          - "You %player_displayname% have: "
          - "%vault_eco_balance% Sheeps!"
      emptyItem:
        Material: "GRAY_STAINED_GLASS_PANE"
        Amount: 1
        Title: ""
    payMenu:
      Title: "ᴘᴀʏ ᴘʟᴀʏᴇʀꜱ"
      pay-message: "How much would you like to pay?"
      nextFullItem:
        Material: "LIME_DYE"
        Amount: 1
        Title: "<green>ɴᴇxᴛ ᴘᴀɢᴇ"
      nextEmptyItem:
        Material: "GRAY_DYE"
        Amount: 1
        Title: "<gray>ɴᴇxᴛ ᴘᴀɢᴇ"
      prevFullItem:
        Material: "LIME_DYE"
        Amount: 1
        Title: "<green>ᴘʀᴇᴠɪᴏᴜꜱ ᴘᴀɢᴇ"
      prevEmptyItem:
        Material: "GRAY_DYE"
        Amount: 1
        Title: "<gray>ᴘʀᴇᴠɪᴏᴜꜱ ᴘᴀɢᴇ"
      playerHead:
        Material: "PLAYERHEAD"
        Amount: 1
        Title: "<#C7FFEA>%player_displayname%"
        Lore:
          - "<#A2A2A2>Left Click to pay this player!"
      emptyItem:
        Material: "GRAY_STAINED_GLASS_PANE"
        Amount: 1
        Title: ""
    balMenu:
      Title: "ᴘʟᴀʏᴇʀ ʙᴀʟᴀɴᴄᴇꜱ"
      nextFullItem:
        Material: "LIME_DYE"
        Amount: 1
        Title: "<green>ɴᴇxᴛ ᴘᴀɢᴇ"
      nextEmptyItem:
        Material: "GRAY_DYE"
        Amount: 1
        Title: "<gray>ɴᴇxᴛ ᴘᴀɢᴇ"
      prevFullItem:
        Material: "LIME_DYE"
        Amount: 1
        Title: "<green>ᴘʀᴇᴠɪᴏᴜꜱ ᴘᴀɢᴇ"
      prevEmptyItem:
        Material: "GRAY_DYE"
        Amount: 1
        Title: "<gray>ᴘʀᴇᴠɪᴏᴜꜱ ᴘᴀɢᴇ"
      playerHead:
        Material: "PLAYERHEAD"
        Amount: 1
        Title: "<#C7FFEA>%player_displayname%'s Balance"
        Lore:
          - "<#A2A2A2>%vault_eco_balance% Sheeps!"
      emptyItem:
        Material: "GRAY_STAINED_GLASS_PANE"
        Amount: 1
        Title: ""
```
