package cn.wolfmc.minecraft.wolfhunter.common.constants

import org.bukkit.Material

val ORE_MATERIAL =
    setOf(
        Material.COAL_ORE,
        Material.DEEPSLATE_COAL_ORE,
        Material.IRON_ORE,
        Material.DEEPSLATE_IRON_ORE,
        Material.COPPER_ORE,
        Material.DEEPSLATE_COPPER_ORE,
        Material.GOLD_ORE,
        Material.DEEPSLATE_GOLD_ORE,
        Material.REDSTONE_ORE,
        Material.DEEPSLATE_REDSTONE_ORE,
        Material.EMERALD_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.LAPIS_ORE,
        Material.DEEPSLATE_LAPIS_ORE,
        Material.DIAMOND_ORE,
        Material.DEEPSLATE_DIAMOND_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE,
    )
val PICKAXE_MATERIAL =
    setOf(
        Material.DIAMOND_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.NETHERITE_PICKAXE,
        Material.WOODEN_PICKAXE,
        Material.STONE_PICKAXE,
    )
val AXE_MATERIAL =
    setOf(
        Material.DIAMOND_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.NETHERITE_AXE,
        Material.WOODEN_AXE,
        Material.STONE_AXE,
    )
val HOE_MATERIAL =
    setOf(
        Material.DIAMOND_HOE,
        Material.IRON_HOE,
        Material.GOLDEN_HOE,
        Material.NETHERITE_HOE,
        Material.WOODEN_HOE,
        Material.STONE_HOE,
    )
val SWORD_MATERIAL =
    setOf(
        Material.DIAMOND_SWORD,
        Material.IRON_SWORD,
        Material.GOLDEN_SWORD,
        Material.NETHERITE_SWORD,
        Material.WOODEN_SWORD,
        Material.STONE_SWORD,
    )
val FURNACE_MATERIAL =
    setOf(
        Material.FURNACE,
        Material.BLAST_FURNACE,
    )
val AUXILIARY_MATERIAL =
    setOf(
        Material.SCAFFOLDING,
        Material.LADDER,
        Material.RAIL,
        Material.ACTIVATOR_RAIL,
        Material.DETECTOR_RAIL,
        Material.POWERED_RAIL,
    )
val REMOTE_WEAPON_MATERIAL =
    setOf(
        Material.BOW,
        Material.CROSSBOW,
    )
val ARMOR_MATERIAL =
    setOf(
        Material.LEATHER_BOOTS,
        Material.LEATHER_CHESTPLATE,
        Material.LEATHER_LEGGINGS,
        Material.LEATHER_HELMET,
        Material.CHAINMAIL_BOOTS,
        Material.CHAINMAIL_CHESTPLATE,
        Material.CHAINMAIL_LEGGINGS,
        Material.CHAINMAIL_HELMET,
        Material.IRON_BOOTS,
        Material.IRON_CHESTPLATE,
        Material.IRON_LEGGINGS,
        Material.IRON_HELMET,
        Material.GOLDEN_BOOTS,
        Material.GOLDEN_CHESTPLATE,
        Material.GOLDEN_LEGGINGS,
        Material.GOLDEN_HELMET,
        Material.DIAMOND_BOOTS,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_HELMET,
        Material.NETHERITE_BOOTS,
        Material.NETHERITE_CHESTPLATE,
        Material.NETHERITE_LEGGINGS,
        Material.NETHERITE_HELMET,
    )
val HARVESTABLE_CROP_MATERIAL =
    setOf(
        Material.WHEAT,
        Material.CARROTS,
        Material.POTATOES,
        Material.BEETROOTS,
        Material.MELON,
        Material.PUMPKIN,
        Material.SWEET_BERRY_BUSH,
    )
val LOG_MATERIAL =
    setOf(
        // 原木
        Material.OAK_LOG,
        Material.SPRUCE_LOG,
        Material.BIRCH_LOG,
        Material.JUNGLE_LOG,
        Material.ACACIA_LOG,
//    Material.CHERRY_LOG,
        Material.DARK_OAK_LOG,
//    Material.MANGROVE_LOG,
        // 去皮原木
        Material.STRIPPED_OAK_LOG,
        Material.STRIPPED_SPRUCE_LOG,
        Material.STRIPPED_BIRCH_LOG,
        Material.STRIPPED_JUNGLE_LOG,
        Material.STRIPPED_ACACIA_LOG,
//    Material.STRIPPED_CHERRY_LOG,
        Material.STRIPPED_DARK_OAK_LOG,
//    Material.STRIPPED_MANGROVE_LOG,
        Material.CRIMSON_STEM,
        Material.WARPED_STEM,
        Material.STRIPPED_CRIMSON_STEM,
        Material.STRIPPED_WARPED_STEM,
    )

private val METAL_MATERIAL =
    setOf(
        Material.COPPER_INGOT,
        Material.IRON_INGOT,
        Material.GOLD_INGOT,
        Material.DIAMOND,
        Material.NETHERITE_INGOT,
    )

private val STONE_MATERIAL =
    setOf(
        Material.STONE,
        Material.COBBLESTONE,
        Material.DEEPSLATE,
        Material.COBBLED_DEEPSLATE,
    )

fun Material.isLog() = this in LOG_MATERIAL

fun Material.isStone() = this in STONE_MATERIAL

fun Material.isMetal() = this in METAL_MATERIAL

fun Material.isRemoteWeapon() = this in REMOTE_WEAPON_MATERIAL

fun Material.isAuxiliaryBlock() = this in AUXILIARY_MATERIAL

fun Material.isFurnace() = this in FURNACE_MATERIAL

fun Material.isSword() = this in SWORD_MATERIAL

fun Material.isArmor() = this in ARMOR_MATERIAL

fun Material.isHoe() = this in HOE_MATERIAL

fun Material.isAxe() = this in AXE_MATERIAL

fun Material.isPickaxe() = this in PICKAXE_MATERIAL

fun Material.isOre() = this in ORE_MATERIAL
