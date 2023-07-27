
summon villager ~ ~ ~ {Tags:["CMD_RefTrades_Donor"]}

# Change VillagerData level, then copy Recipient's State to Donor
execute store result storage ucr_stitch:temp VillagerData.level int 1 run scoreboard players get @s CMD_Temp
data modify entity @e[tag=CMD_RefTrades_Donor,limit=1,sort=nearest] VillagerData set from storage ucr_stitch:temp VillagerData

# Copy Trades from Donor to Recipient
data modify entity @s Offers.Recipes prepend from entity @e[tag=CMD_RefTrades_Donor,limit=1,sort=nearest] Offers.Recipes[2]
data modify entity @s Offers.Recipes prepend from entity @e[tag=CMD_RefTrades_Donor,limit=1,sort=nearest] Offers.Recipes[1]
data modify entity @s Offers.Recipes prepend from entity @e[tag=CMD_RefTrades_Donor,limit=1,sort=nearest] Offers.Recipes[0]

# Get rid of Donor
tp @e[tag=CMD_RefTrades_Donor] ~ -1000 ~
execute as @e[tag=CMD_RefTrades_Donor] run kill @s

# Continue loop
scoreboard players remove @s CMD_Temp 1
execute as @s[scores={CMD_Temp=1..}] run function ucr_stitch:traveling_merchant/refresh_trades_loop