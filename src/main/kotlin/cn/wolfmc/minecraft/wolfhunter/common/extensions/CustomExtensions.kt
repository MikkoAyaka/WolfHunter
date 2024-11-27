package cn.wolfmc.minecraft.wolfhunter.common.extensions

import cn.wolfmc.minecraft.wolfhunter.presentation.i18n.I18n
import net.kyori.adventure.text.Component
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.logging.Level
import java.util.logging.Logger

fun Logger.log(level: Level, msg: Component) {
    this.log(level, msg.legacy())
}
fun Logger.logT(level: Level, key: String) {
    this.log(level, I18n.t(key).legacy())
}
fun JavaPlugin.copyResource(resourceName: String, destFile: File) {
    if (!destFile.exists()) {
        // 确保目录存在
        destFile.parentFile?.mkdirs()
        // 获取资源输入流
        val resourceStream: InputStream = this.javaClass.getResourceAsStream("/$resourceName")
            ?: throw IllegalArgumentException("Resource $resourceName not found in the plugin JAR")
        // 创建输出流并将资源复制过去
        val outputStream: OutputStream = destFile.outputStream()
        resourceStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        logger.info("Resource $resourceName copied to ${destFile.absolutePath}")
    } else {
        logger.info("Resource $resourceName already exists at ${destFile.absolutePath}")
    }
}