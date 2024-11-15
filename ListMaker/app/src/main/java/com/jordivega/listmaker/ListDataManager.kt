package com.jordivega.listmaker

import android.content.Context
import android.content.SharedPreferences

class ListDataManager(private val context: Context) {

    val PREF_NAME = "SharedPreferenceExample"

    fun saveList(list: TaskList) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        sharedPreferences.putStringSet(list.name, list.tasks.toHashSet())
        sharedPreferences.apply()
        sharedPreferences.commit()
    }

    fun removeFromLists(position: Int): ArrayList<TaskList> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sharedPreferenceContents = sharedPreferences.all
        val taskList = ArrayList<TaskList>()
        taskList.removeAt(position)

        return taskList
    }


    fun readLists(): ArrayList<TaskList> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sharedPreferenceContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()

        for (taskList in sharedPreferenceContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)

            taskLists.add(list)
        }

        return taskLists

    }

}