
# essence_temp = amount of free space in essence bar
resource set @s ucr_stitch:mage/essence_temp 500
execute store result score @s CMD_Temp run resource get @s ucr_stitch:mage/essence_real
resource operation @s ucr_stitch:mage/essence_temp -= @s CMD_Temp

# essence_temp = amount to add to the main essence bar
execute store result score @s CMD_Temp run resource get @s ucr_stitch:mage/essence_input
resource operation @s ucr_stitch:mage/essence_temp < @s CMD_Temp

# transfer the necessary amount to the main essence bar
execute store result score @s CMD_Temp run resource get @s ucr_stitch:mage/essence_temp
resource operation @s ucr_stitch:mage/essence_real += @s CMD_Temp
resource operation @s ucr_stitch:mage/essence_input -= @s CMD_Temp

# transfer the remaining essence into the regen bar
execute store result score @s CMD_Temp run resource get @s ucr_stitch:mage/essence_input
resource operation @s ucr_stitch:mage/hungerless_regen_essence += @s CMD_Temp
resource set @s ucr_stitch:mage/essence_input 0
resource set @s ucr_stitch:mage/essence_temp 0
