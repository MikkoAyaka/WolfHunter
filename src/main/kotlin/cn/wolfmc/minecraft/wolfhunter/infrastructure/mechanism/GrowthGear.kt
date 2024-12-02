package cn.wolfmc.minecraft.wolfhunter.infrastructure.mechanism

import cn.wolfmc.minecraft.wolfhunter.common.constants.*
import cn.wolfmc.minecraft.wolfhunter.common.extensions.*
import cn.wolfmc.minecraft.wolfhunter.infrastructure.itemhandler.GrowthGearHandler
import cn.wolfmc.minecraft.wolfhunter.model.component.EventHandlerSet
import cn.wolfmc.minecraft.wolfhunter.model.data.SpecialItem
import cn.wolfmc.minecraft.wolfhunter.model.event.GameEvent
import cn.wolfmc.minecraft.wolfhunter.model.service.ScopeService
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.CraftItemEvent
import taboolib.expansion.chain
import taboolib.platform.util.attacker

object GrowthGear : ScopeService {
    private val eventHandlers = EventHandlerSet()

    // 存储经验
    private val whitelistGears =
        listOf(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.WOODEN_AXE,
            Material.WOODEN_HOE,
            Material.WOODEN_PICKAXE,
            Material.WOODEN_SWORD,
            Material.WOODEN_SHOVEL,
        )

    override fun init() {
        eventHandlers.apply {
            this +=
                EventHandler(CraftItemEvent::class) {
                    val craftType = it.recipe.result.type
                    // TODO 未来支持锄头和铲子
                    if (!craftType.isArmor() && !craftType.isPickaxe() && !craftType.isAxe()) return@EventHandler
                    // 特殊物品发放
                    if (craftType in whitelistGears) {
                        val specialItem = GrowthGearHandler.initItem(it.recipe.result)
                        GrowthGearHandler.updateItem(specialItem)
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
                    val specialItem = GrowthGearHandler.get(item) ?: return@EventHandler
                    specialItem.addExp(calcExp(it.player, specialItem, it.block))
                    val exp = calcExp(it.player, specialItem, it.block)
                    if (exp != 0) specialItem.addExp(exp)
                }
            // 连锁采集经验
            this +=
                EventHandler(GameEvent.RangeMining::class) {
                    val item = it.player.inventory.itemInMainHand
                    if (!item.isSpecialItem(GrowthGearHandler)) return@EventHandler
                    val specialItem = GrowthGearHandler.get(item) ?: return@EventHandler
                    val exp = calcExp(it.player, specialItem, it.block)
                    if (exp != 0) specialItem.addExp(exp)
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
                    val exp = if (defender is Player) (finalDamage * 20).toInt() else (finalDamage * 5).toInt()
                    specialItem.addExp(exp)
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
                        it.addExp(100)
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
            Material.REDSTONE_ORE to 30,
            Material.DEEPSLATE_REDSTONE_ORE to 30,
            Material.EMERALD_ORE to 500,
            Material.DEEPSLATE_EMERALD_ORE to 500,
            Material.LAPIS_ORE to 30,
            Material.DEEPSLATE_LAPIS_ORE to 30,
            Material.DIAMOND_ORE to 100,
            Material.DEEPSLATE_DIAMOND_ORE to 100,
            Material.NETHER_GOLD_ORE to 10,
            Material.NETHER_QUARTZ_ORE to 5,
        )

    /**
     * 计算应该获得的经验
     */
    private fun calcExp(
        player: Player,
        specialItem: SpecialItem,
        block: Block,
    ): Int {
        val type = specialItem.itemStack.type
        if (type.isPickaxe()) {
            if (block.type.isOre()) return oreExpMap[block.type]!!
            if (block.type.isStone()) return 1
        } else if (type.isAxe()) {
            if (block.type.isLog()) return 5
        }
        return 0
    }
}
