
execute store result score @s CMD_GM_Damage run data get entity @s SelectedItem.tag.Damage

function ucr_stitch:greater_mending/loop

item modify entity @s weapon.mainhand ucr_stitch:gm_damage