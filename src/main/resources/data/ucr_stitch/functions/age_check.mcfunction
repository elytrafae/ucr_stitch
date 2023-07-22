
execute as @s store result score @s CMD_AgeCheck run data get entity @s Age

# If adult, set to 1
scoreboard players set @s[scores={CMD_AgeCheck=0..}] CMD_AgeCheck 1

# If child, set to 0
scoreboard players set @s[scores={CMD_AgeCheck=..0}] CMD_AgeCheck 0