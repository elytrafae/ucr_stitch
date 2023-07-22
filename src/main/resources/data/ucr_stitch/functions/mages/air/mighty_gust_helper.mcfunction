
execute as @e[distance=..7] at @s run tp @s ~ ~0.1 ~

tag @s add CMD_Reflect_Player

execute as @e[distance=..7,type=#ucr_stitch:reflectable_projectiles] run data modify entity @s Owner set from entity @p[tag=CMD_Reflect_Player] UUID

tag @s remove CMD_Reflect_Player