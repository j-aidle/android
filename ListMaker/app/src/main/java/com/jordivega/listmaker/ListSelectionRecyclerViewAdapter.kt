package com.jordivega.listmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListSelectionRecyclerViewAdapter(private val lists : ArrayList<TaskList>,
                                       val clickListener: ListSelectionRecyclerViewClickListener)
    : RecyclerView.Adapter<ListSelectionViewHolder>() {

        var listTitles = arrayOf("asix", "dam", "smx")

        interface ListSelectionRecyclerViewClickListener {
            fun listItemClicked(list:TaskList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_selection_view_holder, parent, false)

            return ListSelectionViewHolder(view)
        }

        override fun getItemCount(): Int {
            return lists.size
        }

        override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
            holder.listPosition.text = (position + 1).toString()
            holder.listTitle.text = lists[position].name
            holder.itemView.setOnClickListener {
                clickListener.listItemClicked(lists[position])
            }
        }

        fun addList(list: TaskList) {
            lists.add(list)
            notifyItemInserted(lists.size-1)
        }

        fun removeList(list: TaskList) {
            lists.remove(list)
            notifyItemRemoved(lists.size-1)
            notifyDataSetChanged()
        }
}
