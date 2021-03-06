package com.hiker.presentation.user


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.hiker.R
import com.hiker.domain.entities.Status
import com.hiker.presentation.login.LoginViewModelFactory
import com.hiker.presentation.login.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user_view.*
import java.util.*


class UserView : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_view, container, false)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.VISIBLE
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)
        initViewModel()
        setUpToolbarMenu(userSystemId!!)
        setupObservers(userSystemId)
    }

    private fun setupObservers(userSystemId: String){
        userViewModel.getUser(UUID.fromString(userSystemId)).observe(this@UserView, Observer {
            if (it.status == Status.SUCCESS){
                val user = it.data
                if (user != null){
                    userViewModel.cacheUser(user)
                    userView_firstName_textView.text = user.firstName
                    userView_lastName_textView.text = user.lastName
                    if (user.birthday != null){
                        userView_age_textView.text =  com.hiker.domain.extensions.Period.between(user.birthday, Calendar.getInstance().time).toString()
                    }
                    userView_imageView.visibility = View.VISIBLE
                    imgProgress.visibility = View.GONE
                    userViewModel.setUserThumbnail(userView_imageView, user.facebookId)

                    if (!user.aboutMe.isNullOrEmpty()){
                        userView_aboutMe_textView.text = user.aboutMe
                    }
                    if (!user.phoneNumber.isNullOrEmpty()){
                        userView_phoneNumber_textView.text = user.phoneNumber
                    }
                }
            }
            else{
                val snack = Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_LONG)
                val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
                snack.anchorView = bottomNav
                snack.show()
            }
        })
    }

    private fun setUpToolbarMenu(userSystemId: String){
        val toolbar = view?.findViewById<Toolbar>(R.id.user_profile_toolbar)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.nav_first_edit -> showUserEditView(userSystemId)
                R.id.nav_first_signout -> logoutDialogShow()
            }
            false
        }
    }

    private fun showUserEditView(userId: String){
       val action = UserViewDirections.actionUserViewToUserEditView(userId)
        findNavController().navigate(action)
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
    private fun initViewModel() {
        userViewModel = ViewModelProviders.of(this, UserViewModelFactory(requireContext())).get(UserViewModel::class.java)
    }
}
