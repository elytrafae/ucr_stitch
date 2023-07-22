
# @s = the villager
# @p[tag=CMD_TravelingMerchant] = merchant

data modify storage ucr_stitch:temp great_gossip set value {Type: "trading", Value: 20, Target: [I; 0, 0, 0]}
data modify storage ucr_stitch:temp great_gossip.Target set from entity @a[tag=CMD_TravelingMerchant,limit=1] UUID
data modify entity @s Gossips append from storage ucr_stitch:temp great_gossip