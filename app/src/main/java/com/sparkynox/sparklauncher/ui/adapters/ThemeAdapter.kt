package com.sparkynox.sparklauncher.ui.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sparkynox.sparklauncher.databinding.ItemThemeBinding
import com.sparkynox.sparklauncher.theme.ThemeManager

class ThemeAdapter(
    private val themes: List<ThemeManager.Theme>,
    var currentTheme: ThemeManager.Theme,
    private val onSelect: (ThemeManager.Theme) -> Unit
) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {

    inner class ThemeViewHolder(private val binding: ItemThemeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(theme: ThemeManager.Theme) {
            binding.tvThemeName.text = theme.displayName
            binding.tvThemeDesc.text = theme.description
            binding.ivThemePreview.setImageDrawable(
                ContextCompat.getDrawable(binding.root.context, theme.backgroundRes)
            )
            val isSelected = theme.id == currentTheme.id
            binding.ivSelected.visibility = if (isSelected) View.VISIBLE else View.GONE
            // Highlight card with alpha instead of stroke (CardView compatible)
            binding.root.alpha = if (isSelected) 1.0f else 0.75f
            binding.root.setOnClickListener { onSelect(theme) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ThemeViewHolder(ItemThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) = holder.bind(themes[position])
    override fun getItemCount() = themes.size
}
