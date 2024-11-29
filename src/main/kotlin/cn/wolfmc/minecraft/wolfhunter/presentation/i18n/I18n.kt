package cn.wolfmc.minecraft.wolfhunter.presentation.i18n

import cn.wolfmc.minecraft.wolfhunter.application.api.Contexts
import cn.wolfmc.minecraft.wolfhunter.common.extensions.miniMsg
import net.kyori.adventure.text.Component
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap

object I18n {
    private val LANGUAGES: MutableMap<String, Map<String, String>> = ConcurrentHashMap()
    private var currentLanguage = "zh"

    fun initFiles() {
        val languages = Contexts.plugin.config.getStringList("languages")
        languages.forEach { lang ->
            runCatching {
                Contexts.plugin.saveResource("messages_$lang.yml", false)
            }.onFailure { e ->
                System.err.println("Error saving resource for language: $lang, ${e.message}")
            }
        }
    }

    fun loadLanguages() {
        val languages = Contexts.plugin.config.getStringList("languages")
        languages.forEach { lang ->
            val file = File(Contexts.plugin.dataFolder, "messages_$lang.yml")
            if (file.exists()) {
                runCatching {
                    loadLanguage(lang, file.inputStream())
                }.onFailure { e ->
                    System.err.println("Error loading language file: $lang, ${e.message}")
                }
            } else {
                System.err.println("Language file not found: $lang")
            }
        }
    }

    private fun loadLanguage(lang: String, inputStream: InputStream) {
        val messages: Map<String, String> = Yaml().load(inputStream) ?: emptyMap()
        LANGUAGES[lang] = messages
    }

    fun setLanguage(lang: String) {
        if (LANGUAGES.containsKey(lang)) {
            currentLanguage = lang
        } else {
            System.err.println("Error loading language: $lang")
        }
    }

    /** 获取翻译，支持 {0} {1} 等动态参数替换 */
    fun t(key: String, vararg args: Any): Component {
        val messages = LANGUAGES[currentLanguage]
        val template = messages?.get(key) ?: return Component.text("Missing translation for key: $key")

        val formattedTemplate = args.fold(template) { acc, arg -> acc.replaceFirst(Regex("{${args.indexOf(arg)}}"), arg.toString()) }
        return formattedTemplate.miniMsg()
    }
}
