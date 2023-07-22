
execute store result score @s CMD_GM_Points run xp query @s points
execute store result score @s CMD_GM_Levels run xp query @s levels

tag @s remove CMD_GM_MendIt
tag @s[scores={CMD_GM_Points=2..}] add CMD_GM_MendIt
tag @s[scores={CMD_GM_Levels=1..}] add CMD_GM_MendIt
tag @s[scores={CMD_GM_Damage=..0}] remove CMD_GM_MendIt

xp add @s[tag=CMD_GM_MendIt] -2 points
scoreboard players remove @s[tag=CMD_GM_MendIt] CMD_GM_Points 3
scoreboard players set @s[scores={CMD_GM_Damage=..0}] CMD_GM_Points 0

execute as @s[tag=CMD_GM_MendIt] run function ucr_stitch:greater_mending/loop