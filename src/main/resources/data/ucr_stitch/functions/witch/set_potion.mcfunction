
# Check if there is a potion in main hand
# By checking the following tags: Potion and CustomPotionEffects

execute store success score @s CMD_Temp run data get entity @s SelectedItem.tag.Potion
execute as @s[scores={CMD_Temp=0}] store success score @s CMD_Temp run data get entity @s SelectedItem.tag.CustomPotionEffects
execute as @s[scores={CMD_Temp=1}] run data modify entity @e[tag=CMD_WitchPotion,limit=1] Item set from entity @s SelectedItem

# If neither pass, we assume the potion is in the offhand

execute as @s[scores={CMD_Temp=0}] run data modify entity @e[tag=CMD_WitchPotion,limit=1] Item set from entity @s Inventory[{Slot:-106b}]

data modify entity @e[tag=CMD_WitchPotion,limit=1] Item.id set value "minecraft:splash_potion"

tag @e remove CMD_WitchPotion