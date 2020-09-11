package com.airbnb.epoxy

import android.view.View
import android.widget.FrameLayout
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.airbnb.epoxy.integrationtest.TestActivity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.LEGACY)
class EpoxyVisibilityItemTest {

    @get:Rule
    var activityRule = activityScenarioRule<TestActivity>()

    @Mock
    lateinit var mockEpoxyHolder: EpoxyViewHolder

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testUpdate() {
        activityRule.scenario.onActivity {
            val frameLayout = FrameLayout(it).apply {
                it.setContentView(this)
            }
            val view = View(frameLayout.context).apply {
                layoutParams = FrameLayout.LayoutParams(100, 100)
                frameLayout.addView(this)
            }

            val item = EpoxyVisibilityItem()
            val measured = item.update(view, frameLayout, false)
            assertTrue(measured)
        }
    }

    @Test
    fun testUpdate_visibilityGone() {
        activityRule.scenario.onActivity {
            val frameLayout = FrameLayout(it).apply {
                it.setContentView(this)
            }
            val view = View(frameLayout.context).apply {
                visibility = View.GONE
                layoutParams = FrameLayout.LayoutParams(100, 100)
                frameLayout.addView(this)
            }

            val item = EpoxyVisibilityItem()
            val measured = item.update(view, frameLayout, false)
            assertFalse(measured)
        }
    }

    @Test
    fun testUpdate_visibilityInvisible() {
        activityRule.scenario.onActivity {
            val frameLayout = FrameLayout(it).apply {
                it.setContentView(this)
            }
            val view = View(frameLayout.context).apply {
                visibility = View.INVISIBLE
                layoutParams = FrameLayout.LayoutParams(100, 100)
                frameLayout.addView(this)
            }

            val item = EpoxyVisibilityItem()
            val measured = item.update(view, frameLayout, false)
            assertTrue(measured)
        }
    }

    @Test
    fun testUpdate_noSize() {
        activityRule.scenario.onActivity {
            val frameLayout = FrameLayout(it).apply {
                it.setContentView(this)
            }
            val view = View(frameLayout.context).apply {
                layoutParams = FrameLayout.LayoutParams(0, 0)
                frameLayout.addView(this)
            }

            val item = EpoxyVisibilityItem()
            val measured = item.update(view, frameLayout, false)
            assertFalse(measured)
        }
    }

    @Test
    fun testHandleVisible() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handleVisible(mockEpoxyHolder, false)
            verify(mockEpoxyHolder).visibilityStateChanged(eq(VisibilityState.VISIBLE))
        }
    }

    @Test
    fun testHandleVisible_subsequentCall() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handleVisible(mockEpoxyHolder, false)
            verify(mockEpoxyHolder).visibilityStateChanged(eq(VisibilityState.VISIBLE))
            item.handleVisible(mockEpoxyHolder, false)
            verifyNoMoreInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleVisible_viewInitiallyGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPreViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handleVisible(mockEpoxyHolder, false)
            verifyNoInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleVisible_viewTransitionToGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPostViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handleVisible(mockEpoxyHolder, false)
            verifyNoInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleFocus() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handleFocus(mockEpoxyHolder, false)
            verify(mockEpoxyHolder).visibilityStateChanged(eq(VisibilityState.FOCUSED_VISIBLE))
        }
    }

    @Test
    fun testHandleFocus_subsequentCall() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handleFocus(mockEpoxyHolder, false)
            verify(mockEpoxyHolder).visibilityStateChanged(eq(VisibilityState.FOCUSED_VISIBLE))
            item.handleFocus(mockEpoxyHolder, false)
            verifyNoMoreInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleFocus_viewInitiallyGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPreViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handleFocus(mockEpoxyHolder, false)
            verifyNoInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleFocus_viewTransitionToGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPostViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handleFocus(mockEpoxyHolder, false)
            verifyNoInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandlePartialImpressionVisible() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handlePartialImpressionVisible(mockEpoxyHolder, false, 50)
            verify(mockEpoxyHolder).visibilityStateChanged(eq(VisibilityState.PARTIAL_IMPRESSION_VISIBLE))
        }
    }

    @Test
    fun testHandlePartialImpressionVisible_subsequentCall() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handlePartialImpressionVisible(mockEpoxyHolder, false, 50)
            verify(mockEpoxyHolder).visibilityStateChanged(eq(VisibilityState.PARTIAL_IMPRESSION_VISIBLE))
            item.handlePartialImpressionVisible(mockEpoxyHolder, false, 50)
            verifyNoMoreInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandlePartialImpressionVisible_viewInitiallyGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPreViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handlePartialImpressionVisible(mockEpoxyHolder, false, 100)
            verifyNoInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandlePartialImpressionVisible_viewTransitionToGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPostViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handlePartialImpressionVisible(mockEpoxyHolder, false, 100)
            verifyNoInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleFullImpressionVisible() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handleFullImpressionVisible(mockEpoxyHolder, false)
            verify(mockEpoxyHolder).visibilityStateChanged(eq(VisibilityState.FULL_IMPRESSION_VISIBLE))
        }
    }

    @Test
    fun testHandleFullImpressionVisible_subsequentCall() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handleFullImpressionVisible(mockEpoxyHolder, false)
            verify(mockEpoxyHolder).visibilityStateChanged(eq(VisibilityState.FULL_IMPRESSION_VISIBLE))
            item.handleFullImpressionVisible(mockEpoxyHolder, false)
            verifyNoMoreInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleFullImpressionVisible_viewInitiallyGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPreViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handleFullImpressionVisible(mockEpoxyHolder, false)
            verifyNoInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleFullImpressionVisible_viewTransitionToGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPostViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handleFullImpressionVisible(mockEpoxyHolder, false)
            verifyNoInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleChanged() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handleChanged(mockEpoxyHolder, true)
            verify(mockEpoxyHolder).visibilityChanged(eq(100f), eq(100f), eq(100), eq(100))
        }
    }

    @Test
    fun testHandleChanged_subsequentCall() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate()

            item.handleChanged(mockEpoxyHolder, true)
            verify(mockEpoxyHolder).visibilityChanged(eq(100f), eq(100f), eq(100), eq(100))
            item.handleChanged(mockEpoxyHolder, true)
            verifyNoMoreInteractions(mockEpoxyHolder)
        }
    }

    @Test
    fun testHandleChanged_viewInitiallyGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPreViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handleChanged(mockEpoxyHolder, true)
            verify(mockEpoxyHolder).visibilityChanged(eq(0f), eq(0f), eq(0), eq(0))
        }
    }

    @Test
    fun testHandleChanged_viewTransitionToGone() {
        activityRule.scenario.onActivity {
            val item = it.createViewAndUpdate(
                onPostViewAdded = { view ->
                    view.visibility = View.GONE
                }
            )

            item.handleChanged(mockEpoxyHolder, true)
            verify(mockEpoxyHolder).visibilityChanged(eq(0f), eq(0f), eq(0), eq(0))
        }
    }

    /**
     * Helper function that:
     * * Creates a [FrameLayout] and sets it as the content view in the activity.
     * * Creates a [View] and adds it to the [FrameLayout]. Default size is 100px x 100px.
     * * Creates and returns an [EpoxyVisibilityItem] and calls [EpoxyVisibilityItem.update] on it.
     *
     * @param onPreViewAdded invoked before the view is added to the [FrameLayout]. Use this to
     * customize the view (e.g set visibility, change size, etc).
     * @param onPostViewAdded invoked after the view is added to the [FrameLayout]. Use this to
     * customize the view (e.g set visibility, change size, etc).
     */
    private fun TestActivity.createViewAndUpdate(
        detachEvent: Boolean = false,
        onPreViewAdded: (View) -> (Unit) = {},
        onPostViewAdded: (View) -> (Unit) = {}
    ): EpoxyVisibilityItem {
        val frameLayout = FrameLayout(this)
        setContentView(frameLayout)
        val view = View(frameLayout.context).apply {
            layoutParams = FrameLayout.LayoutParams(100, 100)
            onPreViewAdded.invoke(this)
            frameLayout.addView(this)
            onPostViewAdded.invoke(this)
        }

        return EpoxyVisibilityItem().apply {
            update(view, frameLayout, detachEvent)
        }
    }
}
