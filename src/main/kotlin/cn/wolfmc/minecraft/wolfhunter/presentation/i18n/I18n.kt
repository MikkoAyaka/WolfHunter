package cn.wolfmc.minecraft.wolfhunter.presentation.i18n

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap


object I18n {
    private val MINI_MESSAGE = MiniMessage.miniMessage()
    private val LANGUAGES: MutableMap<String, Map<String, String>> = ConcurrentHashMap()
    private var currentLanguage = "zh"

    fun loadLanguage(lang: String) {
        loadLanguage(lang, FileInputStream("messages_$lang.yml"))
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
        return MINI_MESSAGE.deserialize(template!!)
    }
}
