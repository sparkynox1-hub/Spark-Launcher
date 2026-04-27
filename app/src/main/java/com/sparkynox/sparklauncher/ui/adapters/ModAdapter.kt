package com.sparkynox.sparklauncher.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sparkynox.sparklauncher.R
import com.sparkynox.sparklauncher.data.models.Mod
import com.sparkynox.sparklauncher.data.models.ModSource
import com.sparkynox.sparklauncher.databinding.ItemModBinding

class ModAdapter(
    private val onDownload: (Mod) -> Unit,
    private val onInfo: (Mod) -> Unit
) : ListAdapter<Mod, ModAdapter.ModViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Mod>() {
        override fun areItemsTheSame(old: Mod, new: Mod) = old.id == new.id
        override fun areContentsTheSame(old: Mod, new: Mod) = old == new
    }

    inner class ModViewHolder(private val binding: ItemModBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mod: Mod) {
            binding.tvModName.text = mod.name
            binding.tvModAuthor.text = mod.author
            binding.tvModDesc.text = mod.description
            binding.tvDownloads.text = formatCount(mod.downloadCount)

            // Source badge
            binding.tvSource.text = when (mod.source) {
                ModSource.MODRINTH -> "MODRINTH"
                ModSource.CURSEFORGE -> "CURSEFORGE"
                else -> ""
            }
            binding.tvSource.setBackgroundColor(
                when (mod.source) {
                    ModSource.MODRINTH -> 0xFF1BD96A.toInt()  // Modrinth green
                    ModSource.CURSEFORGE -> 0xFFE8771E.toInt() // CurseForge orange
                    else -> 0xFF444444.toInt()
                }
            )

            // Icon — use Glide with disk cache to reduce network calls
            if (mod.iconUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(mod.iconUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_mod_placeholder)
                    .error(R.drawable.ic_mod_placeholder)
                    .override(96, 96)  // Downsample — memory optimization
                    .into(binding.ivModIcon)
            } else {
                binding.ivModIcon.setImageResource(R.drawable.ic_mod_placeholder)
            }

            binding.btnDownload.setOnClickListener { onDownload(mod) }
            binding.root.setOnClickListener { onInfo(mod) }
        }

        private fun formatCount(n: Long): String = when {
            n >= 1_000_000 -> "%.1fM".format(n / 1_000_000.0)
            n >= 1_000 -> "%.1fK".format(n / 1_000.0)
            else -> n.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModViewHolder {
        val binding = ItemModBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ModViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModViewHolder, position: Int) =
        holder.bind(getItem(position))

    // Recycle Glide requests when views are recycled
    override fun onViewRecycled(holder: ModViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.itemView.context).clear(holder.itemView)
    }
}
