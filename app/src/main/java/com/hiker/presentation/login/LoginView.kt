package com.hiker.presentation.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.hiker.R
import com.hiker.data.repository.UserRepositoryImpl
import kotlinx.android.synthetic.main.fragment_login_view.*
import java.lang.Exception

private const val TAG : String = "LoginView"

class LoginView : Fragment() {

    private lateinit var facebookCallbackManager: CallbackManager
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initMapViewModel()
        initFacebookButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private fun initFacebookButton(){
        try{
            facebookCallbackManager = CallbackManager.Factory.create()
            login_button.fragment = this;
            LoginManager.getInstance().registerCallback(facebookCallbackManager,  object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    loginViewModel.getUserByFacebookId(loginResult.accessToken.userId).observe(this@LoginView, Observer {
                        if (it != null){
                            it.firstName
                            Log.i(TAG, "Facebook user already exist")
                            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                            if (sharedPref != null) {
                                with (sharedPref.edit()){
                                    putString(getString(R.string.preferences_userSystemId), it.id)
                                    commit()
                                }
                            }
                            findNavController().navigate(LoginViewDirections.actionLoginViewToMapView())
                        }
                        else{
                            Log.i(TAG, "Facebook user does not exist")
                            loginViewModel.registerUserFromFacebook(loginResult.accessToken.token)
                            findNavController().navigate(LoginViewDirections.actionLoginViewToMapView())
                        }
                    })
                }

                override fun onCancel() {
                    Toast.makeText(requireContext(), "onCancel()", Toast.LENGTH_LONG).show()

                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(requireContext(), "Facebook error", Toast.LENGTH_LONG).show()
                }
            })
        }
        catch (ex: Exception){
            Toast.makeText(requireContext(), ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initMapViewModel() {
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(requireContext())).get(LoginViewModel::class.java)
    }
}