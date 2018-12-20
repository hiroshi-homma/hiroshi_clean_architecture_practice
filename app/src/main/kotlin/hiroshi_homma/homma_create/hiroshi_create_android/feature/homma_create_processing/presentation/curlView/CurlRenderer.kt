/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.curlView

import java.util.Vector

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import android.opengl.GLSurfaceView
import android.opengl.GLU

/**
 * Actual renderer class.
 *
 * @author harism
 */
class CurlRenderer
/**
 * Basic constructor.
 */
(private val mObserver: Observer) : GLSurfaceView.Renderer {
    // Background fill color.
    private var mBackgroundColor: Int = 0
    // Curl meshes used for static and dynamic rendering.
    private val mCurlMeshes: Vector<CurlMesh> = Vector()
    private val mMargins = RectF()
    // Page rectangles.
    private val mPageRectLeft: RectF = RectF()
    private val mPageRectRight: RectF = RectF()
    // View mode.
    private var mViewMode = SHOW_ONE_PAGE
    // Screen size.
    private var mViewportWidth: Int = 0
    private var mViewportHeight: Int = 0
    // Rect for render area.
    private val mViewRect = RectF()

    /**
     * Adds CurlMesh to this renderer.
     */
    @Synchronized
    fun addCurlMesh(mesh: CurlMesh) {
        removeCurlMesh(mesh)
        mCurlMeshes.add(mesh)
    }

    /**
     * Returns rect reserved for left or right page. Value page should be
     * PAGE_LEFT or PAGE_RIGHT.
     */
    fun getPageRect(page: Int): RectF? {
        if (page == PAGE_LEFT) {
            return mPageRectLeft
        } else if (page == PAGE_RIGHT) {
            return mPageRectRight
        }
        return null
    }

    @Synchronized
    override fun onDrawFrame(gl: GL10) {

        mObserver.onDrawFrame()

        gl.glClearColor(Color.red(mBackgroundColor) / 255f,
                Color.green(mBackgroundColor) / 255f,
                Color.blue(mBackgroundColor) / 255f,
                Color.alpha(mBackgroundColor) / 255f)
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT)
        gl.glLoadIdentity()

        for (i in mCurlMeshes.indices) {
            mCurlMeshes[i].onDrawFrame(gl)
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)
        mViewportWidth = width
        mViewportHeight = height

        val ratio = width.toFloat() / height
        mViewRect.top = 1.0f
        mViewRect.bottom = -1.0f
        mViewRect.left = -ratio
        mViewRect.right = ratio
        updatePageRects()

        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        GLU.gluOrtho2D(gl, mViewRect.left, mViewRect.right,
                mViewRect.bottom, mViewRect.top)

        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glShadeModel(GL10.GL_SMOOTH)
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
        gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST)
        gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST)
        gl.glEnable(GL10.GL_LINE_SMOOTH)
        gl.glDisable(GL10.GL_DEPTH_TEST)
        gl.glDisable(GL10.GL_CULL_FACE)

        mObserver.onSurfaceCreated()
    }

    /**
     * Removes CurlMesh from this renderer.
     */
    @Synchronized
    fun removeCurlMesh(mesh: CurlMesh) {
        while (mCurlMeshes.remove(mesh))
        ;
    }

    /**
     * Change background/clear color.
     */
    fun setBackgroundColor(color: Int) {
        mBackgroundColor = color
    }

    /**
     * Set margins or padding. Note: margins are proportional. Meaning a value
     * of .1f will produce a 10% margin.
     */
    @Synchronized
    fun setMargins(left: Float, top: Float, right: Float,
                   bottom: Float) {
        mMargins.left = left
        mMargins.top = top
        mMargins.right = right
        mMargins.bottom = bottom
        updatePageRects()
    }

    /**
     * Sets visible page count to one or two. Should be either SHOW_ONE_PAGE or
     * SHOW_TWO_PAGES.
     */
    @Synchronized
    fun setViewMode(viewmode: Int) {
        if (viewmode == SHOW_ONE_PAGE) {
            mViewMode = viewmode
            updatePageRects()
        } else if (viewmode == SHOW_TWO_PAGES) {
            mViewMode = viewmode
            updatePageRects()
        }
    }

    /**
     * Translates screen coordinates into view coordinates.
     */
    fun translate(pt: PointF) {
        pt.x = mViewRect.left + mViewRect.width() * pt.x / mViewportWidth
        pt.y = mViewRect.top - -mViewRect.height() * pt.y / mViewportHeight
    }

    /**
     * Recalculates page rectangles.
     */
    private fun updatePageRects() {
        if (mViewRect.width() == 0f || mViewRect.height() == 0f) {
            return
        } else if (mViewMode == SHOW_ONE_PAGE) {
            mPageRectRight.set(mViewRect)
            mPageRectRight.left += mViewRect.width() * mMargins.left
            mPageRectRight.right -= mViewRect.width() * mMargins.right
            mPageRectRight.top += mViewRect.height() * mMargins.top
            mPageRectRight.bottom -= mViewRect.height() * mMargins.bottom

            mPageRectLeft.set(mPageRectRight)
            mPageRectLeft.offset(-mPageRectRight.width(), 0f)

            val bitmapW = (mPageRectRight.width() * mViewportWidth / mViewRect
                    .width()).toInt()
            val bitmapH = (mPageRectRight.height() * mViewportHeight / mViewRect
                    .height()).toInt()
            mObserver.onPageSizeChanged(bitmapW, bitmapH)
        } else if (mViewMode == SHOW_TWO_PAGES) {
            mPageRectRight.set(mViewRect)
            mPageRectRight.left += mViewRect.width() * mMargins.left
            mPageRectRight.right -= mViewRect.width() * mMargins.right
            mPageRectRight.top += mViewRect.height() * mMargins.top
            mPageRectRight.bottom -= mViewRect.height() * mMargins.bottom

            mPageRectLeft.set(mPageRectRight)
            mPageRectLeft.right = (mPageRectLeft.right + mPageRectLeft.left) / 2
            mPageRectRight.left = mPageRectLeft.right

            val bitmapW = (mPageRectRight.width() * mViewportWidth / mViewRect
                    .width()).toInt()
            val bitmapH = (mPageRectRight.height() * mViewportHeight / mViewRect
                    .height()).toInt()
            mObserver.onPageSizeChanged(bitmapW, bitmapH)
        }
    }

    /**
     * Observer for waiting render engine/state updates.
     */
    interface Observer {
        /**
         * Called from onDrawFrame called before rendering is started. This is
         * intended to be used for animation purposes.
         */
        fun onDrawFrame()

        /**
         * Called once page size is changed. Width and height tell the page size
         * in pixels making it possible to update textures accordingly.
         */
        fun onPageSizeChanged(width: Int, height: Int)

        /**
         * Called from onSurfaceCreated to enable texture re-initialization etc
         * what needs to be done when this happens.
         */
        fun onSurfaceCreated()
    }

    companion object {

        // Constant for requesting left page rect.
        const val PAGE_LEFT = 1
        // Constant for requesting right page rect.
        const val PAGE_RIGHT = 2
        // Constants for changing view mode.
        const val SHOW_ONE_PAGE = 1
        const val SHOW_TWO_PAGES = 2
    }
}
