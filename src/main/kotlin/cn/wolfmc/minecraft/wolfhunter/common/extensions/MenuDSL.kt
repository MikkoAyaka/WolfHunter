package cn.wolfmc.minecraft.wolfhunter.common.extensions

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.command.CommandSender
import org.bukkit.inventory.InventoryHolder

object VirtualInventoryHolder: InventoryHolder {
    override fun getInventory(): Inventory {
        return Bukkit.createInventory(null, 9)
    }
}

// 扩展函数：为插件创建菜单
fun JavaPlugin.menu(name: String, size: Int, builder: InventoryBuilder.() -> Unit): Inventory {
    val inventory = Bukkit.createInventory(VirtualInventoryHolder, size, Component.text(name))
    val inventoryBuilder = InventoryBuilder(inventory).apply(builder)
    inventoryBuilder.setup()
    return inventory
}

// GUI 菜单构建器
class InventoryBuilder(private val inventory: Inventory) {
    private val itemBuilders = mutableListOf<ItemBuilder>()

    // 设置物品并处理点击事件
    fun item(slot: Int, builder: ItemBuilder.() -> Unit) {
        val itemBuilder = ItemBuilder(slot).apply(builder)
        itemBuilders.add(itemBuilder)
    }

    // 刷新物品
    fun setup() {
        // 在这里处理每个物品的刷新逻辑
        for (itemBuilder in itemBuilders) {
            inventory.setItem(itemBuilder.slot, itemBuilder.build())
            // 如果该物品需要定时刷新
            itemBuilder.refreshTime?.apply {
                Runnable {
                    val newItem = itemBuilder.build()  // 更新物品
                    inventory.setItem(itemBuilder.slot, newItem)
                }.runTaskTimerAsync(0, this)
            }
        }
    }
}

// GUI 物品构建器
class ItemBuilder(val slot: Int) {
    private var material: Material = Material.STONE  // 默认物品
    private var displayName: String = ""
    private var lore: List<String> = listOf()
    var refreshTime: Long? = null // 刷新时间（ticks）

    // 设置物品内容
    fun material(material: Material) {
        this.material = material
    }

    fun displayName(name: String) {
        this.displayName = name
    }

    fun lore(vararg lines: String) {
        this.lore = lines.toList()
    }

    // 设置刷新时间（以 tick 为单位）
    fun refreshPeriod(ticks: Long) {
        this.refreshTime = ticks
    }

    // 构建物品
    fun build(): ItemStack {
        val item = ItemStack(material)
        val meta: ItemMeta = item.itemMeta ?: throw IllegalArgumentException("ItemMeta is null")
        meta.displayName(displayName.miniMsg())
        meta.lore(lore.map { it.miniMsg() })
        item.itemMeta = meta
        return item
    }
}



// 监听菜单事件
object MenuListener : Listener {
    @EventHandler
    fun onMenuClick(event: InventoryClickEvent) {
        val clickedItem = event.currentItem ?: return
        val player = event.whoClicked as? org.bukkit.entity.Player ?: return
        if (event.clickedInventory?.holder != VirtualInventoryHolder) return
        event.isCancelled = true
        // 根据物品内容或点击位置执行相应的操作
        if (clickedItem.hasItemMeta()) {
            val meta = clickedItem.itemMeta
            if (meta?.displayName == "Example Item") {
                player.sendMessage("You clicked the Example Item!")
            }
        }
    }
}
