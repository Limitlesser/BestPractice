package com.limitlesser.bestpractice.preference

import android.content.Context
import android.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


open class PrefDelegate<T>(val context: Context,
                           val name: String,
                           private val defaultValue: T) : ReadWriteProperty<Any?, T> {

    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (defaultValue) {
            is Boolean -> preferences.getBoolean(name, defaultValue)
            is String -> preferences.getString(name, defaultValue)
            is Int -> preferences.getInt(name, defaultValue)
            is Float -> preferences.getFloat(name, defaultValue)
            is Long -> preferences.getLong(name, defaultValue)
            is Set<*> -> preferences.getStringSet(name, defaultValue as MutableSet<String>)
            is List<*> -> preferences.getString(name, "").split(",").filter { it.isNotEmpty() }
            else -> throw IllegalArgumentException("illegal type")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val edit = preferences.edit()
        @Suppress("UNCHECKED_CAST")
        when (defaultValue) {
            is Boolean -> edit.putBoolean(name, value as Boolean)
            is String -> edit.putString(name, value as String)
            is Int -> edit.putInt(name, value as Int)
            is Float -> edit.putFloat(name, value as Float)
            is Long -> edit.putLong(name, value as Long)
            is Set<*> -> edit.putStringSet(name, value as MutableSet<String>)
            is List<*> -> edit.putString(name, (value as List<String>).joinToString(","))
            else -> throw IllegalArgumentException("illegal type")
        }
        edit.apply()
    }

}