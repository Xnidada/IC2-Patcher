package mods.su5ed.ic2patcher;

import java.io.File;
import java.util.Objects;


public class IC2PatcherConfig extends net.minecraftforge.common.config.Configuration {

    private static IC2PatcherConfig config;
    /**
     * 是否启用新的 UU 计算系统。
     */
    public final boolean newUUCalculator;
    /**
     * 是否启用实验性的 World Reference 修复补丁。
     */
    public final boolean worldReferenceSwitch;
    /**
     * 是否启用实验性的推送/拉取升级模块 tick 逻辑。
     */
    public final boolean enableUpgradeTickInterval;
    /**
     * 是否让流体单元具备普通水桶的行为。
     */
    public final boolean bucketCell;
    /**
     * 是否增强量子套装的夜视功能（移除失明效果）。
     */
    public final boolean qsNVBuff;
    /**
     * 是否将纳米套装的摔落伤害减免改为吸收伤害。
     */
    public final boolean nsFDBuff;
    /**
     * 是否让发电机更加节省燃料（能量缓存充满时限制燃料消耗速率）。
     */
    public final boolean genEffBuff;
    /**
     * 电炉最多存储的经验值。
     */
    public final float electricFurnaceMaxExperience;
    /**
     * 推送/拉取升级模块的 tick 间隔。
     */
    public final int upgradeTickInterval;
    /**
     * 推送失败后的冷却时间。
     */
    public final int pushCooldown;
    /**
     * 电加热器每个线圈提供的 HU。
     */
    public final int huPerCoil;
    /**
     * 液体热交换器每个热交换器提供的 HU。
     */
    public final int huPerExc;

    protected IC2PatcherConfig(String file) {
        super(new File("config/" + file + ".cfg"));

        bucketCell = this.getBoolean("bucketFluidCell", "patches", true,
            "为流体单元添加类似水桶的行为，使其可以拾取不与方块相邻的流体。");

        enableUpgradeTickInterval = this.getBoolean("enableUpgradeTickInterval", "patches", false,
            "[实验性] 强制推送/拉取升级模块每 x 个 tick 执行一次，少数情况除外。关闭时使用 IC2 默认行为。");

        upgradeTickInterval = this.getInt("upgradeSlotTickInterval", "patches", 5, 1, 100,
            "[实验性] 强制推送/拉取升级模块每 x 个 tick 执行一次，少数情况除外。\n" +
                "- 推送：输出槽已满或传输失败时执行 tick；传输失败会触发冷却。\n" +
                "- 拉取：输出槽或输入槽已满时跳过 tick。\n ");

        pushCooldown = this.getInt("pushUpgradeCooldown", "patches", 20, 1, 1200,
            "[实验性] 确定推送升级模块在物品传输失败后的冷却时间（tick）。");

        worldReferenceSwitch = this.getBoolean("worldReferenceSwitch", "patches", false,
            "[实验性] 更改 EnergyNet 处理和存储世界引用的方式，可能修复电线随机断开的问题。\n" +
                "启用后将替换原有补丁中的实现。如果启用该补丁后 EnergyNet 出现问题，请反馈。\n" +
                "建议在启用前备份世界。");

        newUUCalculator = this.getBoolean("newUUMatterCalculator", "patches", false,
            "[实验性] 使用完全自定义的方案替换原有 UU 物质计算器。\n" +
                "这会影响 UU 值的计算，使其可能比默认值更加合理，并可能解决其他模组导致原方案崩溃的兼容性问题。");

        qsNVBuff = this.getBoolean("buffedQuantumSuitNV", "balance", true,
            "使量子套装的夜视功能完全自动化：环境过亮时移除失明效果，并在这种情况下仅移除夜视效果。");

        nsFDBuff = this.getBoolean("buffedNanoSuitFallDamageReduction", "balance", true,
            "使纳米套装吸收最多 10 点摔落伤害，而不是在摔落伤害超过 10 点时承受全部伤害。");

        genEffBuff = this.getBoolean("efficientGenerators", "balance", true,
            "改变发电机行为：能量缓存充满时限制燃料消耗速率，使其更加节省燃料。");

        electricFurnaceMaxExperience = this.getFloat("electricFurnaceMaxExperience", "balance", 100.0F, 0.0F, Float.MAX_VALUE,
            "电炉最多存储的经验值。");

        huPerCoil = this.getInt("huPerCoil", "balance", 10,
            0, 1000, "确定电加热器每个线圈的效率 [每个线圈产生的 HU]。");

        huPerExc = this.getInt("huPerExchanger", "balance", 10, 1, 1000,
            "确定液体热交换器每个热交换器的效率 [每个热交换器传输的 HU]。");

        if (this.hasChanged()) this.save();
    }

    public static IC2PatcherConfig get() {
        if (config == null) {
            set(new IC2PatcherConfig("IC2-Patcher"));
            config.load();
        }
        return config;
    }

    protected static void set(IC2PatcherConfig config) {
        IC2PatcherConfig.config = Objects.requireNonNull(config);
    }
}