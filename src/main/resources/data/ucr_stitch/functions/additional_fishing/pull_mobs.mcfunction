
# Credit to IndustrialCraft on Github for this clever method!

# Fishing Guardian
execute as @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "guardian"}}}] positioned as @s run summon item ~ ~ ~ {Tags: [CMD_FishingMomentumAdd], Age: 5920, PickupDelay: 32767, Item: {id: "minecraft:stone", Count: 1b}, Passengers: [{id: "minecraft:guardian"}]}
data modify entity @e[type=item,limit=1,sort=nearest,tag=CMD_FishingMomentumAdd] Motion set from entity @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "guardian"}}}] Motion
kill @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "guardian"}}}]

# Fishing Drowned
execute as @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "drowned"}}}] positioned as @s run summon item ~ ~ ~ {Tags: [CMD_FishingMomentumAdd], Age: 5920, PickupDelay: 32767, Item: {id: "minecraft:stone", Count: 1b}, Passengers: [{id: "minecraft:drowned"}]}
data modify entity @e[type=item,limit=1,sort=nearest,tag=CMD_FishingMomentumAdd] Motion set from entity @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "drowned"}}}] Motion
kill @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "drowned"}}}]

# Fishing Elder Guardian
execute as @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "elder_guardian"}}}] positioned as @s run summon item ~ ~ ~ {Tags: [CMD_FishingMomentumAdd], Age: 5920, PickupDelay: 32767, Item: {id: "minecraft:stone", Count: 1b}, Passengers: [{id: "minecraft:elder_guardian"}]}
data modify entity @e[type=item,limit=1,sort=nearest,tag=CMD_FishingMomentumAdd] Motion set from entity @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "elder_guardian"}}}] Motion
kill @e[type=item,limit=1,sort=nearest,nbt={Item: {tag: {CMD_FishingSpawnType: "elder_guardian"}}}]