package com.hiker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hiker.toolbar.FragmentToolbar
import com.hiker.toolbar.ToolbarManager

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ToolbarManager(builder(), view).prepareToolbar()
    }

    protected abstract fun builder(): FragmentToolbar
}