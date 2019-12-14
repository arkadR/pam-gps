package com.pam.gps.ui.bindingAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.firebase.storage.FirebaseStorage
import com.pam.gps.glideModules.GlideApp
import timber.log.Timber

@BindingAdapter("storageUri")
fun ImageView.bindStorageUri(storageUri: String?) {
  if (storageUri.isNullOrBlank()) {
    Timber.e("storage uri not present, uri = $storageUri")
    return
  }
  Timber.d("storage uri = $storageUri")
  val ref = FirebaseStorage.getInstance().getReference(storageUri)
  GlideApp
    .with(context)
    .load(ref)
    .into(this)
}