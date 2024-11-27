package cn.wolfmc.minecraft.wolfhunter.presentation.i18n

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import net.kyori.adventure.text.Component
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap


object I18n {
    private val LANGUAGES: MutableMap<String, Map<String, String>> = ConcurrentHashMap()
    private var currentLanguage = "zh"

    fun initFiles() {
        Contexts.plugin.apply {
            saveResource("messages_zh.yml", false)
        }
    }

    fun loadLanguage(lang: String) {
        loadLanguage(lang, FileInputStream(File(Contexts.plugin.dataFolder,"messages_$lang.yml")))
    }

    // 加载语言文件
    private fun loadLanguage(lang: String, inputStream: InputStream?) {
        val yaml = Yaml()
        val messages = yaml.load<Map<String, String>>(inputStream)
        LANGUAGES[lang] = messages
    }

    // 设置当前语言
    fun setLanguage(lang: String) {
        if (LANGUAGES.containsKey(lang)) {
            currentLanguage = lang
        } else {
            System.err.println("Language not loaded: $lang")
        }
    }

    /**
     * 获取翻译，支持 {0} {1} 等动态参数替换
     */
    fun t(key: String, vararg args: Any): Component {
        val messages = LANGUAGES[currentLanguage]
        if (messages == null || !messages.containsKey(key)) {
            return Component.text("Missing translation for key: $key")
        }
        var template = messages[key]
        for (i in args.indices) {
            template = template!!.replace("{$i}", args[i].toString())
        }
        return template!!.miniMsg()
    }
}
