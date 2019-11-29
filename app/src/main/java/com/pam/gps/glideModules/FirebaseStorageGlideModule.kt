package com.pam.gps.glideModules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import java.io.InputStream

//https://github.com/firebase/FirebaseUI-Android/tree/master/storage
@GlideModule
class FirebaseStorageGlideModule : AppGlideModule() {
  override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    registry.append(
      StorageReference::class.java,
      InputStream::class.java,
      FirebaseImageLoader.Factory()
    )
  }
}