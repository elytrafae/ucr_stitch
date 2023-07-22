
playsound minecraft:entity.witch.throw player @a ~ ~ ~ 1 1

summon potion ~ ~-0.3 ~ {Motion:[0.0,-0.5,0.0],Tags:["CMD_WitchPotion"],Item:{id:"minecraft:splash_potion",Count:1b,tag:{Potion:"minecraft:weakness"}}}
data modify entity @e[tag=CMD_WitchPotion,limit=1] Owner set from entity @s UUID

tag @e remove CMD_WitchPotion