package com.hiker.presentation.user


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.login.LoginManager
import com.hiker.R
import com.hiker.data.remote.api.ApiConsts
import com.hiker.presentation.login.LoginViewModel
import com.hiker.presentation.login.LoginViewModelFactory
import com.hiker.presentation.login.UserViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_view.*
import java.time.LocalDateTime
import java.time.Period
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class UserView : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initMapViewModel()
        setUpToolbarMenu()
        setupObservers()
    }

    private fun setupObservers(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)

        userViewModel.getUser(UUID.fromString(userSystemId)).observe(this@UserView, Observer {
            userView_firstName_textView.text = it?.firstName
            userView_lastName_textView.text = it?.lastName
            userView_age_textView.text =  com.hiker.domain.extensions.Period.between(it!!.birthday, Calendar.getInstance().time).toString()
            userViewModel.setMountainThumbnail(userView_imageView, it?.facebookId)
        })
    }

    private fun setUpToolbarMenu(){
        val toolbar = view?.findViewById<Toolbar>(R.id.user_profile_toolbar)
        toolbar?.inflateMenu(R.menu.user_profile_menu)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.nav_first_signout -> logoutDialogShow()
            }
            false
        }
    }

    private fun logoutDialogShow(){
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Uwaga")
            .setMessage("Czy chcesz się się wylogować z aplikacji?")
            .setPositiveButton("Tak") { _, _ ->
                LoginManager.getInstance().logOut()
                finishAffinity(requireActivity())
            }
            .setNegativeButton("Nie", /* listener = */ null)
            .show()
    }
    private fun initMapViewModel() {
        userViewModel = ViewModelProviders.of(this, LoginViewModelFactory(requireContext())).get(UserViewModel::class.java)
    }
}
