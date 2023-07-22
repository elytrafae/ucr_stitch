
tag @s add CMD_Reflect_Player

# Summon armor stand that helps with direction calculations.
summon armor_stand ~ ~ ~ {NoGravity:1b,Silent:1b,Invulnerable:1b,ShowArms:0b,Small:1b,Marker:1b,Invisible:1b,NoBasePlate:1b,PersistenceRequired:1b,Tags:["CMD_Reflect_Helper"],DisabledSlots:4144959}

function ucr_stitch:reflect/calc_motion
title @p actionbar ["","DEBUG: ",{"score":{"name":"@e[tag=CMD_Reflect_Helper,limit=1]","objective":"CMD_ReflectXM"}}," ",{"score":{"name":"@e[tag=CMD_Reflect_Helper,limit=1]","objective":"CMD_ReflectYM"}}," ",{"score":{"name":"@e[tag=CMD_Reflect_Helper,limit=1]","objective":"CMD_ReflectZM"}}]
function ucr_stitch:reflect/reflect_projectiles

tag @s remove CMD_Reflect_Player
tag @e remove CMD_Reflect_Projectiles
kill @e[tag=CMD_Reflect_Helper]