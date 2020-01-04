package com.pam.gps.ui.home.map

import android.content.Context
import android.media.ExifInterface
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.pam.gps.extensions.BitmapFactory
import timber.log.Timber

class IconMarkerManagerRenderer(private val context: Context, googleMap: GoogleMap, clusterManager: ClusterManager<MapMarker>)
  : DefaultClusterRenderer<MapMarker>(context, googleMap, clusterManager) {

  private val iconGenerator: IconGenerator = IconGenerator(context)
  private val imageView = ImageView(context)

  init {
    iconGenerator.setContentView(imageView)
    imageView.layoutParams.height = 100
    imageView.layoutParams.width = 100
  }

  override fun onBeforeClusterItemRendered(item: MapMarker, markerOptions: MarkerOptions) {
    val icon = BitmapFactory.decodeFileWithDimensions(item.localPath!!, 64, 64)
    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title) //Don't know what the title changes
  }
}
