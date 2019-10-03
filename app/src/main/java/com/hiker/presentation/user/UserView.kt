package com.hiker.presentation.user


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import com.facebook.login.LoginManager
import com.hiker.R
import kotlinx.android.synthetic.main.fragment_user_view.*

/**
 * A simple [Fragment] subclass.
 */
class UserView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupLoginButton()
    }

    private fun setupLoginButton(){
        logoutButton.setOnClickListener {
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
    }


}
