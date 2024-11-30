package cn.wolfmc.minecraft.wolfhunter

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

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
}
