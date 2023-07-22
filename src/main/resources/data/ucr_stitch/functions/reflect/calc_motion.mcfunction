
execute anchored eyes run tp @e[tag=CMD_Reflect_Helper,limit=1] @s

execute as @e[tag=CMD_Reflect_Helper,limit=1] store result score @s CMD_ReflectXM run data get entity @s Pos[0] 100
execute as @e[tag=CMD_Reflect_Helper,limit=1] store result score @s CMD_ReflectYM run data get entity @s Pos[1] 100
execute as @e[tag=CMD_Reflect_Helper,limit=1] store result score @s CMD_ReflectZM run data get entity @s Pos[2] 100

execute as @e[tag=CMD_Reflect_Helper,limit=1] run tp @s ^ ^ ^10

execute as @e[tag=CMD_Reflect_Helper,limit=1] store result score @s CMD_Temp run data get entity @s Pos[0] 100
scoreboard players operation @e[tag=CMD_Reflect_Helper,limit=1] CMD_ReflectXM -= @e[tag=CMD_Reflect_Helper,limit=1] CMD_Temp

execute as @e[tag=CMD_Reflect_Helper,limit=1] store result score @s CMD_Temp run data get entity @s Pos[1] 100
scoreboard players operation @e[tag=CMD_Reflect_Helper,limit=1] CMD_ReflectYM -= @e[tag=CMD_Reflect_Helper,limit=1] CMD_Temp

execute as @e[tag=CMD_Reflect_Helper,limit=1] store result score @s CMD_Temp run data get entity @s Pos[2] 100
scoreboard players operation @e[tag=CMD_Reflect_Helper,limit=1] CMD_ReflectZM -= @e[tag=CMD_Reflect_Helper,limit=1] CMD_Temp