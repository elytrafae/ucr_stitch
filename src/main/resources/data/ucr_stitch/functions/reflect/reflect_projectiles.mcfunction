
tp @e[tag=CMD_Reflect_Helper,limit=1] ^ ^ ^1

execute as @e[tag=CMD_Reflect_Helper,limit=1] at @s run tp ~ ~1.25 ~

execute as @e[tag=CMD_Reflect_Helper,limit=1] at @s run tag @e[type=#ucr_stitch:reflectable_projectiles,distance=..2] add CMD_Reflect_Projectiles

execute anchored eyes run tp @e[tag=CMD_Reflect_Projectiles] ~ ~1.25 ~
execute as @e[tag=CMD_Reflect_Projectiles] at @s run function ucr_stitch:reflect/set_self_reflected