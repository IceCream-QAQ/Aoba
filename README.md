# Aoba

Aoba 是一个基于 Kotlin/JVM 的对于主流云服务厂商 API 的封装库。  
Aoba 基于 Kotlin/JVM 与 Kotlin 协程，使用 Okhttp 进行纯异步网络请求。  

Aoba 是一个第三方项目，与任何云服务厂商无关。  
Aoba 并不能完整的实现云服务厂商的所有 API 功能，也无法保证 API 能跟随云服务 API 的更新而更新。

## 对于 Java 开发者

Aoba 将会提供对应的 Async 与 Blocking API，以方便 Java 开发者使用。

## 对于 Spring 用户

Aoba 通过 `kotlinx-serialization-json` 进行 JSON 序列化与反序列化。  
Spring 会判断是否存在 `kotlinx-serialization-json`。  
如果存在则会 优先 对 `kotlinx-serialization-json` 支持的类 使用 `kotlinx-serialization-json` 进行 JSON 序列化与反序列化。  
对于存在 后处理器 修改返回值的情况，请手动配置 Spring MVC 序列化器，否则可能产生叠加效果导致异常。