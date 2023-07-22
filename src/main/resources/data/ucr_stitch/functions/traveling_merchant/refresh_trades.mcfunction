
# @s = the villager that needs the refresh!

# Temp = Villager's Level
execute store result score @s CMD_Temp run data get entity @s VillagerData.level 1

# Workaround for some stuff. To complicated to explain here. 
# Just know that this will be used as the Recipient's Villager Data from now on
data modify storage ucr_stitch:temp VillagerData set from entity @s VillagerData

data modify entity @s Offers.Recipes set value []

function ucr_stitch:traveling_merchant/refresh_trades_loop