package com.lzp.jetpack.dsl

import android.util.Log

class Dependency {
    val libraries = arrayListOf<String>()

    fun implementation(lib: String) {
        libraries.add(lib)
    }
}

fun dependencies(block: Dependency.() -> Unit): List<String> {
    val dependency = Dependency()
    dependency.block()
    return dependency.libraries
}

class Td {
    var content = ""

    fun html() = "\n\t\t<td>$content</td>"
}

class Tr {
    private val children = arrayListOf<Td>()

    fun td(block: Td.() -> String) {
        val td = Td()
        td.content = td.block()
        children.add(td)
    }

    fun html(): String {
        val builder = StringBuilder()
        builder.append("\n\t<tr>")
        for (child in children) {
            builder.append(child.html())
        }
        builder.append("</tr>")
        return builder.toString()
    }
}

class Table {
    private val children = arrayListOf<Tr>()

    fun tr(block: Tr.() -> Unit) {
        val tr = Tr()
        tr.block()
        children.add(tr)
    }

    fun html(): String {
        val builder = StringBuilder()
        builder.append("<table>")
        for (child in children) {
            builder.append(child.html())
        }
        builder.append("\n</table>")
        return builder.toString()
    }
}

fun table(block: Table.() -> Unit): String {
    val table = Table()
    table.block()
    return table.html()
}

fun main() {
    val html = table {
        tr {
            td { "1" }
            td { "2" }
            td { "3" }
        }
        tr {
            td { "4" }
            td { "5" }
            td { "6" }
        }
    }

    Log.d("lzp", html)
}