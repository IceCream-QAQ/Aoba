rootProject.name = "Aoba"

fun includeProject(name: String, dir: String? = null) {
    include(name)
    dir?.let { project(name).projectDir = file(it) }
}

fun isp(name: String, dir: String? = null) {
    includeProject(":isp-$name", dir ?: "isp/$name")
}

isp("tencent")
//isp("volcengine")