package com.s26.personlist

import java.io.Serializable
import java.util.*

data class Person (
    val name:String,
    val age:Int,
    val isMarried:Boolean=false
):Serializable {

    val uuid: Long = initUuid()
    private fun initUuid(): Long{
        var value: Long
        do {
            value = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
        }while (value < 0)
        return value
    }

    override fun toString(): String = "name; $name, age: $age, ${if (isMarried)"既婚" else "未婚"};"
}

