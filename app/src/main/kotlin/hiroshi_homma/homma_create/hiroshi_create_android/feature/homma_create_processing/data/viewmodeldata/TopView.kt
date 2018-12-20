package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.viewmodeldata

import android.os.Parcel
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.KParcelable
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.parcelableCreator

data class TopView(val id: Int, val poster: String) :
        KParcelable {
    companion object {
        @JvmField val CREATOR = parcelableCreator(
                ::TopView)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(poster)
        }
    }
}
