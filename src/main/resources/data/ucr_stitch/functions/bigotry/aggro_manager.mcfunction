
# Turn on aggro!
execute at @s run tag @e[type=#ucr_stitch:bigots,distance=..20] add CMD_BigotTag
tag @s add CMD_BigotTarget
execute as @e[tag=CMD_BigotTag] run data modify entity @s AngryAt set from entity @e[tag=CMD_BigotTarget,limit=1] UUID
execute as @e[tag=CMD_BigotTag] run data modify entity @s AngerTime set value 60

tag @s remove CMD_BigotTarget
tag @e[tag=CMD_BigotTag] remove CMD_BigotTag


# Turn off aggro :3
execute at @s run tag @e[type=#ucr_stitch:bigots,distance=21..40] add CMD_BigotTag
execute as @e[tag=CMD_BigotTag] run data modify entity @s AngryAt set from entity @s UUID
execute as @e[tag=CMD_BigotTag] run data modify entity @s AngerTime set value 0

tag @e[tag=CMD_BigotTag] remove CMD_BigotTag