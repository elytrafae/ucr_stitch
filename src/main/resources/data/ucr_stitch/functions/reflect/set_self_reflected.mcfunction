
data modify entity @s Owner set from entity @p[tag=CMD_Reflect_Player] UUID

execute store result entity @s Motion[0] double -0.002 run scoreboard players get @e[tag=CMD_Reflect_Helper,limit=1] CMD_ReflectXM
execute store result entity @s Motion[1] double -0.002 run scoreboard players get @e[tag=CMD_Reflect_Helper,limit=1] CMD_ReflectYM
execute store result entity @s Motion[2] double -0.002 run scoreboard players get @e[tag=CMD_Reflect_Helper,limit=1] CMD_ReflectZM