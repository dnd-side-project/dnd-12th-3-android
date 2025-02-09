package com.dnd.safety.data.datasorce

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dnd.safety.domain.model.Media
import com.dnd.safety.domain.model.MediaType
import javax.inject.Inject

class MediaPagingSource @Inject constructor(
    private val contentResolver: ContentResolver,
) : PagingSource<Int, Media>() {

    companion object {
        private const val PAGE_SIZE = 30
        private const val INITIAL_PAGE = 0
    }

    override fun getRefreshKey(state: PagingState<Int, Media>) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val offset = page * PAGE_SIZE
            val mediaList = mutableListOf<Media>()

            val queryUri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.DATE_ADDED
            )

            val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (?, ?)"
            val selectionArgs = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )

            val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

            contentResolver.query(
                queryUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                if (cursor.moveToPosition(offset)) {
                    var count = 0
                    do {
                        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                        val mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
                        val isVideo = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO

                        val contentUri = if (isVideo) {
                            ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                        } else {
                            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        }

                        mediaList.add(
                            Media(
                                uri = contentUri,
                                type = if (isVideo) MediaType.VIDEO else MediaType.IMAGE
                            )
                        )
                        count++
                    } while (cursor.moveToNext() && count < PAGE_SIZE)
                }
            }

            LoadResult.Page(
                data = mediaList,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (mediaList.size < PAGE_SIZE) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}