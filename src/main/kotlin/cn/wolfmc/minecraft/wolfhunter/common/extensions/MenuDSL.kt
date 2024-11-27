package cn.wolfmc.minecraft.wolfhunter.common.extensions

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object VirtualInventoryHolder : InventoryHolder {
    override fun getInventory(): Inventory {
        return Bukkit.createInventory(null, 9)
    }
}

private val inventoryWithMenu: MutableMap<Inventory, MenuBuilder> = mutableMapOf()
private val menuWithItems: MutableMap<MenuBuilder, MutableMap<Int, MenuItemBuilder>> = mutableMapOf()
private val itemWithMenus: MutableMap<MenuItemBuilder, MutableSet<MenuBuilder>> = mutableMapOf()

fun indexMenuAndItem(menu: MenuBuilder, item: MenuItemBuilder) {
    inventoryWithMenu[menu.inventory] = menu
    menuWithItems.getOrPut(menu) { mutableMapOf() }[item.slot] = item
    itemWithMenus.getOrPut(item) { mutableSetOf() }.add(menu)
}

fun Inventory.findMenu() = inventoryWithMenu[this]
fun MenuBuilder.findItem(slot: Int) = menuWithItems[this]?.get(slot)
fun MenuBuilder.findItems() = menuWithItems[this]?.values ?: setOf()
fun MenuItemBuilder.findMenus() = itemWithMenus.getOrPut(this) { mutableSetOf() }

fun menu(name: String, size: Int, builder: MenuBuilder.() -> Unit): Inventory {
    val menuBuilder = MenuBuilder(name, size).apply(builder)
    menuBuilder.setup()
    return menuBuilder.inventory
}

fun MenuBuilder.item(slot: Int, builder: MenuItemBuilder.() -> Unit) {
    val menuItemBuilder = MenuItemBuilder(slot).apply(builder)
    indexMenuAndItem(this, menuItemBuilder)
}

// GUI 菜单构建器
class MenuBuilder(
    val name: String,
    val size: Int,
    val ownerUuid: UUID? = null
) {
    val inventory: Inventory = Bukkit.createInventory(VirtualInventoryHolder, size, name.miniMsg())

    // 刷新物品
    fun setup() {
        // 在这里处理每个物品的刷新逻辑
        for (itemBuilder in findItems()) {
            inventory.setItem(itemBuilder.slot, itemBuilder.build())
            // 如果该物品需要定时刷新
            itemBuilder.refreshPeriod?.apply {
                Runnable {
                    val newItem = itemBuilder.build()  // 更新物品
                    inventory.setItem(itemBuilder.slot, newItem)
                }.runTaskTimerAsync(0, this)
            }
        }
    }
}

// GUI 物品构建器
class MenuItemBuilder(val slot: Int) {

    var material: Material = Material.STONE
    var displayName: String = ""
    var lore: List<String> = listOf()
    var amount: Int = 1
    var refreshPeriod: Long? = null
    var leftClick: Player.() -> Unit = {}
    var rightClick: Player.() -> Unit = {}
    var leftShiftClick: Player.() -> Unit = {}
    var rightShiftClick: Player.() -> Unit = {}

    fun build(): ItemStack {
        val item = ItemStack(material, amount)
        val meta: ItemMeta = item.itemMeta ?: throw IllegalArgumentException("ItemMeta is null")
        meta.displayName(displayName.miniMsg())
        meta.lore(lore.map { it.miniMsg() })
        item.itemMeta = meta
        return item
    }

}

object MenuDSL : Listener {

    fun init(plugin: JavaPlugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onMenuClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        if (event.clickedInventory?.holder != VirtualInventoryHolder) return
        event.isCancelled = true
        event.clickedInventory?.findMenu()?.findItem(event.slot)?.run {
            event.apply {
                if (isLeftClick) {
                    if (isShiftClick) leftShiftClick(player) else leftClick(player)
                }
                if (isRightClick) {
                    if (isShiftClick) rightShiftClick(player) else rightClick(player)
                }
            }
        }
    }
}
