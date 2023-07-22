
#scoreboard objectives add CMD_Temp dummy
#scoreboard objectives add CMD_Brittle_Health health
#scoreboard objectives add CMD_Brittle_LastHP dummy
#scoreboard objectives add CMD_Brittle_MaxHP dummy

# Calculate half of the MaxHP
scoreboard players set @s CMD_Temp 2
execute store result score @s CMD_Brittle_MaxHP run attribute @s minecraft:generic.max_health get
scoreboard players operation @s CMD_Brittle_MaxHP /= @s CMD_Temp

# Calculate Damage taken in the last tick
scoreboard players operation @s CMD_Temp = @s CMD_Brittle_LastHP
scoreboard players operation @s CMD_Temp -= @s CMD_Brittle_Health

scoreboard players operation @s CMD_Temp -= @s CMD_Brittle_MaxHP

# Finally, set the LastHP as the HP this tick,
# so calculations work on the next tick!
scoreboard players operation @s CMD_Brittle_LastHP = @s CMD_Brittle_Health

# Output: if CMD_Temp is positive, then true, and kill the player. Otherwise false.