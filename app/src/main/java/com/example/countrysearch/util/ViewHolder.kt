package com.example.countrysearch.util

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class ViewHolder<T : ViewDataBinding>(val binding: T) :
    RecyclerView.ViewHolder(binding.root)