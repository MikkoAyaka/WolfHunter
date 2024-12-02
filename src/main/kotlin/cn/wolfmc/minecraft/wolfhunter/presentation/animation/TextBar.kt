package cn.wolfmc.minecraft.wolfhunter.presentation.animation

object TextBar {
    fun defaultStyle(
        progress: Double,
        length: Int,
    ): String {
        require(progress in 0.0..1.0) { "Progress must be between 0.0 and 1.0" }
        require(length > 0) { "Length must be greater than 0" }

        val filled = '■'
        val empty = '□'

        // 计算填充字符的数量
        val filledCount = (progress * length).toInt()

        // 构造进度条
        val filledPart = filled.toString().repeat(filledCount)
        val emptyPart = empty.toString().repeat(length - filledCount)

        return "<gradient:green:red>$filledPart$emptyPart</gradient>"
    }
}
