
power grant @s ucr_stitch:witch/wand_effect
resource operation @s ucr_stitch:witch/wand_effect_damage > @s CMD_WBDMG
scoreboard players remove @s CMD_WBDMG 1

tag @s add CMD_DamageDonor
execute as @e[type=!minecraft:player,distance=..3] run scoreboard players operation @s CMD_WBDMG2 = @s CMD_WBDMG
execute as @e[type=!minecraft:player,distance=..3] run scoreboard players operation @s CMD_WBDMG > @e[tag=CMD_DamageDonor,limit=1] CMD_WBDMG
tag @s remove CMD_DamageDonor

execute as @e[type=!minecraft:player,distance=..3] at @s if score @s CMD_WBDMG > @s CMD_WBDMG2 run function ucr_stitch:witch/wand_bounce