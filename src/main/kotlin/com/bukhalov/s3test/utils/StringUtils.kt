package com.bukhalov.s3test.utils

object StringUtils {
    fun transliterate(path: String): String {
        var resultPath = ""

        path.lowercase().forEach {
            val replacementChar: String = when (it) {
                'а' -> "a"
                'б' -> "b"
                'в' -> "v"
                'г' -> "g"
                'д' -> "d"
                'е' -> "e"
                'ё' -> "yo"
                'ж' -> "zh"
                'з' -> "z"
                'и' -> "i"
                'й' -> "j"
                'к' -> "k"
                'л' -> "l"
                'м' -> "m"
                'н' -> "n"
                'о' -> "o"
                'п' -> "p"
                'р' -> "r"
                'с' -> "s"
                'т' -> "t"
                'у' -> "u"
                'ф' -> "f"
                'х' -> "kh"
                'ц' -> "c"
                'ч' -> "ch"
                'ш' -> "sh"
                'щ' -> "shch"
                'ъ' -> ""
                'ы' -> "y"
                'ь' -> ""
                'э' -> "eh"
                'ю' -> "yu"
                'я' -> "ya"
                else -> it.toString()
            }
            resultPath += replacementChar
        }
        resultPath = resultPath.replace(Regex(" "), "-")
        resultPath = resultPath.replace(Regex("_"), "-")
        resultPath = resultPath.replace(Regex("[^0-9a-z.\\-/]"), "")
        resultPath = resultPath.replace(Regex("[.]{2,}"), ".")
        resultPath = resultPath.replace(Regex("[-]{2,}"), "-")
        return resultPath
    }

    fun toJavaName(pString: String?): String? {
        return if (pString == null) {
            null
        } else {
            val buf = StringBuffer("_")
            for (element in pString) {
                val c = element.code
                if (c == '_'.code) {
                    buf.append("__")
                } else if (c == '.'.code) {
                    buf.append("_x")
                } else if (c == '-'.code) {
                    buf.append("_s")
                } else if (c == '$'.code) {
                    buf.append("_S")
                } else if ((c < 'A'.code || c > 'Z'.code) && (c < 'a'.code || c > 'z'.code) && (c < '0'.code || c > '9'.code) && c < 256) {
                    buf.append('_')
                    buf.append(hexDigit(c shr 12 and 15))
                    buf.append(hexDigit(c shr 8 and 15))
                    buf.append(hexDigit(c shr 4 and 15))
                    buf.append(hexDigit(c and 15))
                } else {
                    buf.append(c.toChar())
                }
            }
            buf.toString()
        }
    }

    private fun hexDigit(pDigit: Int): Char {
        return if (pDigit < 10) (48 + pDigit).toChar() else (97 + pDigit - 10).toChar()
    }

    fun updatePath(path: String, id: String) = if (path.contains(".")) {
        "${path.substringBeforeLast(".")}_$id.${path.substringAfterLast(".")}"
    } else "${path}_$id"
}
