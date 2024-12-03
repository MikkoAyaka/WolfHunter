package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.application.uhc.UHCGameService
import cn.wolfmc.minecraft.wolfhunter.common.constants.*
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler.GrowthGearHandler
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import cn.wolfmc.minecraft.wolfhunter.presentation.sound.Sounds
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import taboolib.expansion.chain
import taboolib.platform.util.attacker

object GrowthGear : ScopeService {
    private val eventHandlers = EventHandlerSet()
    var expMultiple: Double = 1.0

    private val whitelistGears =
        listOf(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.WOODEN_AXE,
//            Material.WOODEN_HOE,
            Material.WOODEN_PICKAXE,
            Material.WOODEN_SWORD,
//            Material.WOODEN_SHOVEL,
        )

    private fun tryUpdateItem(
        player: Player,
        itemStack: ItemStack?,
    ) {
        if (itemStack?.isSpecialItem(GrowthGearHandler) != true) return
        val specialItem = GrowthGearHandler.get(itemStack)!!
        GrowthGearHandler.updateItem(player, specialItem, itemStack)
    }

    fun tryInitItem(
        player: Player,
        itemStack: ItemStack?,
    ) {
        if (itemStack == null) return
        if (itemStack.type !in whitelistGears) return
        if (itemStack.isSpecialItem(GrowthGearHandler)) return
        val specialItem = GrowthGearHandler.initItem(itemStack)
        GrowthGearHandler.updateItem(player, specialItem, itemStack)
    }

    override fun init() {
        if (Contexts.gameService == UHCGameService) expMultiple = 4.0
        eventHandlers.apply {
            // 交互时刷新特殊物品
            this +=
                EventHandler(PlayerInteractEvent::class) {
                    tryInitItem(it.player, it.item)
                    tryUpdateItem(it.player, it.item)
                }
            // 受伤时刷新护甲
            this +=
                EventHandler(EntityDamageEvent::class) {
                    val player = it.entity as? Player ?: return@EventHandler
                    player.inventory.armorContents.forEach { armor ->
                        tryInitItem(player, armor)
                        tryUpdateItem(player, armor)
                    }
                }
            // 玩家捡起无属性的初始物品
            this +=
                EventHandler(EntityPickupItemEvent::class) {
                    val player = it.entity as? Player ?: return@EventHandler
                    tryInitItem(player, it.item.itemStack)
                    tryUpdateItem(player, it.item.itemStack)
                }

            this +=
                EventHandler(CraftItemEvent::class) {
                    val craftType = it.currentItem?.type ?: return@EventHandler
                    // TODO 未来支持锄头和铲子
                    if (!craftType.isArmor() && !craftType.isPickaxe() && !craftType.isAxe()) return@EventHandler
                    // 特殊物品发放
                    if (craftType in whitelistGears) {
                        tryInitItem(it.whoClicked as Player, it.currentItem)
                    } else {
                        // 禁止合成高阶装备
                        it.isCancelled = true
                    }
                }
            // 采集经验
            this +=
                EventHandler(BlockBreakEvent::class) {
                    val item = it.player.inventory.itemInMainHand
                    if (!item.isSpecialItem(GrowthGearHandler)) return@EventHandler
                    val specialItem = GrowthGearHandler.get(item)!!
                    specialItem.addExp(calcExp(it.player, specialItem, it.block))
                    val exp = calcExp(it.player, specialItem, it.block)
                    if (exp != 0.0) {
                        specialItem.addExp(exp)
                        it.player.playSound(Sounds.EXP_PICKUP)
                    }
                }
            // 连锁采集经验
            this +=
                EventHandler(GameEvent.RangeMining::class) {
                    val item = it.player.inventory.itemInMainHand
                    if (!item.isSpecialItem(GrowthGearHandler)) return@EventHandler
                    val specialItem = GrowthGearHandler.get(item) ?: return@EventHandler
                    val exp = calcExp(it.player, specialItem, it.block)
                    if (exp != 0.0) specialItem.addExp(exp)
                }
            // PVE / PVP 经验
            this +=
                EventHandler(EntityDamageByEntityEvent::class) {
                    val attacker = it.attacker
                    if (attacker !is Player) return@EventHandler
                    val weapon = attacker.inventory.itemInMainHand
                    if (!weapon.isSpecialItem(GrowthGearHandler)) return@EventHandler
                    val defender = it.entity
                    val finalDamage = it.finalDamage
                    val specialItem = GrowthGearHandler.get(weapon)!!
                    val exp = if (defender is Player) finalDamage * 20 else finalDamage * 5
                    specialItem.addExp(exp)
                }
            // 装备锻造
            this +=
                EventHandler(InventoryClickEvent::class) {
                    if (it.cursor?.type != Material.IRON_INGOT) return@EventHandler
                    if (it.currentItem?.isSpecialItem(GrowthGearHandler) != true) return@EventHandler
                    val specialItem = GrowthGearHandler.get(it.currentItem!!)!!
                    specialItem.addExp(125.0 * it.cursor!!.amount)
                    val player = it.whoClicked as Player
                    player.setItemOnCursor(null)
                    player.playSound(Sounds.ANVIL_USE)
                    tryUpdateItem(player, it.currentItem)
                    it.isCancelled = true
                }
        }
    }

    private var job: TBJob? = null

    override fun enable() {
        eventHandlers.registerAll()
        // 每分钟获得 100 点经验
        job =
            chain {
                while (true) {
                    wait(20 * 60)
                    GrowthGearHandler.allItems().forEach {
                        it.addExp(100.0)
                    }
                }
            }
    }

    override fun disable() {
        eventHandlers.unregisterAll()
        job?.cancel()
    }

    private val oreExpMap =
        mutableMapOf(
            Material.COAL_ORE to 5,
            Material.DEEPSLATE_COAL_ORE to 5,
            Material.IRON_ORE to 20,
            Material.DEEPSLATE_IRON_ORE to 20,
            Material.COPPER_ORE to 5,
            Material.DEEPSLATE_COPPER_ORE to 5,
            Material.GOLD_ORE to 30,
            Material.DEEPSLATE_GOLD_ORE to 30,
            Material.REDSTONE_ORE to 20,
            Material.DEEPSLATE_REDSTONE_ORE to 20,
            Material.LAPIS_ORE to 20,
            Material.DEEPSLATE_LAPIS_ORE to 20,
            Material.EMERALD_ORE to 1000,
            Material.DEEPSLATE_EMERALD_ORE to 1000,
            Material.DIAMOND_ORE to 300,
            Material.DEEPSLATE_DIAMOND_ORE to 300,
            Material.NETHER_GOLD_ORE to 20,
            Material.NETHER_QUARTZ_ORE to 30,
        )

    /**
     * 计算应该获得的经验
     */
    private fun calcExp(
        player: Player,
        specialItem: SpecialItem.GrowthGear,
        block: Block,
    ): Double {
        val type = specialItem.type
        if (type.isPickaxe()) {
            if (block.type.isOre()) return oreExpMap[block.type]!!.toDouble()
            if (block.type.isStone()) return 1.0
        } else if (type.isAxe()) {
            if (block.type.isLog()) return 10.0
        }
        return 0.0
    }
}
