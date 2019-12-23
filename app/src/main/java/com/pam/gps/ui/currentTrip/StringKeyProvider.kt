package com.pam.gps.ui.currentTrip

import androidx.recyclerview.selection.ItemKeyProvider

class StringKeyProvider(
  private val adapter: PictureSelectAdapter
) : ItemKeyProvider<String>(SCOPE_CACHED) {
  override fun getKey(position: Int): String? = adapter.imageUris[position]
  override fun getPosition(key: String): Int = adapter.imageUris.indexOf(key)
}