
execute store success score @s CMD_Temp run playerex refund_attribute @s playerex:constitution true
execute as @s[scores={CMD_Temp=1..}] run function ucr_stitch:skill_point_reset/constitution