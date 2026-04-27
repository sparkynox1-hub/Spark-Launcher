package com.sparkynox.sparklauncher.ui.adapters

import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.data.models.GameInstance
import com.sparkynox.sparklauncher.databinding.ItemInstanceBinding
import java.text.SimpleDateFormat
import java.util.*

class InstanceAdapter(
    private val onSelect: (GameInstance) -> Unit,
    private val onEdit: (GameInstance) -> Unit,
    private val onDelete: (GameInstance) -> Unit
) : ListAdapter<GameInstance, InstanceAdapter.InstanceViewHolder>(DiffCallback) {

    private var selectedId: String? = null

    companion object DiffCallback : DiffUtil.ItemCallback<GameInstance>() {
        override fun areItemsTheSame(a: GameInstance, b: GameInstance) = a.id == b.id
        override fun areContentsTheSame(a: GameInstance, b: GameInstance) = a == b
    }

    inner class InstanceViewHolder(private val binding: ItemInstanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(instance: GameInstance) {
            binding.tvInstanceName.text = instance.name
            binding.tvVersion.text = buildVersionString(instance)
            binding.tvLastPlayed.text = buildLastPlayedString(instance.lastPlayed)

            // Selected state
            val isSelected = instance.id == selectedId
            binding.viewSelected.visibility = if (isSelected) View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                selectedId = instance.id
                notifyDataSetChanged()
                onSelect(instance)
            }

            binding.btnMore.setOnClickListener { view ->
                PopupMenu(view.context, view).apply {
                    inflate(R.menu.menu_instance)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_edit -> { onEdit(instance); true }
                            R.id.action_delete -> { onDelete(instance); true }
                            else -> false
                        }
                    }
                    show()
                }
            }
        }

        private fun buildVersionString(instance: GameInstance): String {
            val loader = if (instance.modLoader != "vanilla")
                " • ${instance.modLoader.replaceFirstChar { it.uppercase() }}"
            else ""
            return "${instance.versionName}$loader"
        }

        private fun buildLastPlayedString(ts: Long): String {
            if (ts == 0L) return "Never played"
            val now = System.currentTimeMillis()
            val diff = now - ts
            return when {
                diff < 60_000 -> "Just now"
                diff < 3_600_000 -> "${diff / 60_000}m ago"
                diff < 86_400_000 -> "${diff / 3_600_000}h ago"
                diff < 604_800_000 -> "${diff / 86_400_000}d ago"
                else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(ts))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstanceViewHolder {
        val binding = ItemInstanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InstanceViewHolder, position: Int) =
        holder.bind(getItem(position))
}
