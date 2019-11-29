package com.pam.gps.ui.bindingAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.firebase.storage.FirebaseStorage
import com.pam.gps.glideModules.GlideApp

@BindingAdapter("storageUri")
fun ImageView.bindStorageUri(storageUri: String) {
  val ref = FirebaseStorage.getInstance().getReference(storageUri)
  GlideApp
    .with(context)
    .load(ref)
    .into(this)
}