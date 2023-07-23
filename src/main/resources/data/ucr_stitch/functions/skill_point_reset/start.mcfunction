
# Give a bunch of refunds, it is capped at how many points were spent
playerex refund @s 500

# Start recursively removing skill points, one by one . . .
function ucr_stitch:skill_point_reset/constitution
function ucr_stitch:skill_point_reset/dexterity
function ucr_stitch:skill_point_reset/intelligence
function ucr_stitch:skill_point_reset/luckiness
function ucr_stitch:skill_point_reset/strength

# Play a sound and display a message to let the player know that the operation was successful!
playsound minecraft:entity.experience_orb.pickup player @s ~ ~ ~ 1 0.5
playsound minecraft:entity.allay.item_thrown player @s ~ ~ ~ 1 0.8
tellraw @s {"text":"Your power has been severed from your body ...","bold":true,"color":"dark_red"}
tellraw @s {"text":"... But in its place sprouted the will to start anew.","bold":true,"color":"gold"}