package com.sparkynox.sparklauncher.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            // Preview image
            binding.ivThemePreview.setImageDrawable(
                ContextCompat.getDrawable(binding.root.context, theme.backgroundRes)
            )

            // Selected indicator
            val isSelected = theme.id == currentTheme.id
            binding.ivSelected.visibility = if (isSelected) View.VISIBLE else View.GONE
            binding.root.strokeColor = if (isSelected)
                ContextCompat.getColor(binding.root.context, theme.accentColor)
            else
                ContextCompat.getColor(binding.root.context, com.sparkynox.sparklauncher.R.color.divider)
            binding.root.strokeWidth = if (isSelected) 2 else 1

            binding.root.setOnClickListener {
                onSelect(theme)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val binding = ItemThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) =
        holder.bind(themes[position])

    override fun getItemCount() = themes.size
}
