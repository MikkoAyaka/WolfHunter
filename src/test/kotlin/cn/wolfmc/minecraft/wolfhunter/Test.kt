package cn.wolfmc.minecraft.wolfhunter

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

object Test {
    private lateinit var server: ServerMock
    private lateinit var plugin: WolfHunterPlugin

    @JvmStatic
    @BeforeAll
    fun setup() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(WolfHunterPlugin::class.java)
    }

    @JvmStatic
    @AfterAll
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun test1() {
        val p = server.addPlayer("MikkoAyaka")
        p.sendMessage("你好~")
    }
}
