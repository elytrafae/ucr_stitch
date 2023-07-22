
execute store result score @s CMD_Temp run data get entity @s Age
scoreboard players add @s CMD_Temp 2400
execute store result entity @s Age int 1 run scoreboard players get @s CMD_Temp