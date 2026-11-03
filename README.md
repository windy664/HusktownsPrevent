<div align="center">

<img src="assets/banner.svg" alt="HusktownsPrevent" width="100%">

# HusktownsPrevent

**HuskTowns 城镇 Mod 物品保护** —— 在他人城镇范围内拦截科技扳手等 Mod 物品交互，保护城镇设施。

![Platform](https://img.shields.io/badge/Platform-Spigot%20%2F%20Paper-62bda8?style=flat-square&logo=minecraft&logoColor=white)
![Minecraft](https://img.shields.io/badge/MC-1.20.x-8a9a5b?style=flat-square)
![Java](https://img.shields.io/badge/Java-17-f89820?style=flat-square&logo=openjdk&logoColor=white)
![Depends](https://img.shields.io/badge/softdepend-PlaceholderAPI-e2b90f?style=flat-square)

[功能](#-功能) · [配置](#️-配置)

</div>

## ✨ 功能

- **城镇边界拦截**：玩家在非自己所属城镇的区域内右键使用 `disabledItems` 中的物品时取消交互
- **自定义提示**：拦截消息可配（默认中文，支持颜色代码）
- **debug 模式**：输出判定日志，便于排查
- **命令映射**：支持把指定命令重定向（例如把 `spawn` 映射到 `huskhomes:spawn`）

默认内置拦截：Create / PowAH / TechReborn / AE2 / Advanced Reborn / Industrial Revolution / Modern Industrialization 各家扳手。

> 同系列插件：[BentoBoxModFix](https://github.com/windy664/BentoBoxModFix) · [IridiumSkyblockModTweaks](https://github.com/windy664/IridiumSkyblockModTweaks) · [PlotsquaredModFix](https://github.com/windy664/PlotsquaredModFix)

## ⚙️ 配置

```yaml
disabledItems:
  - CREATE_WRENCH
  - POWAH_WRENCH
  - TECHREBORN_WRENCH
  - AE2_CERTUS_QUARTZ_WRENCH
  - MODERN_INDUSTRIALIZATION_WRENCH

messages: "&8[&c✘&8] &f你没有权限在这个城镇使用这个扳手！"

commands:
  spawn: "huskhomes:spawn %player_name%"

debug: false
```

## 🔨 构建

```bash
./gradlew build
```
