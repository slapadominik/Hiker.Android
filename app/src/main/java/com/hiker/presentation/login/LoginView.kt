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
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import com.hiker.R
import com.hiker.data.converters.asUserBrief
import com.hiker.domain.entities.Status
import kotlinx.android.synthetic.main.fragment_login_view.*
import java.lang.Exception


class LoginView : Fragment() {

    private lateinit var facebookCallbackManager: CallbackManager
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLoginViewModel()
        AccessToken.refreshCurrentAccessTokenAsync()
        loginViewModel.isUserLoggedIn(AccessToken.getCurrentAccessToken()).observe(viewLifecycleOwner, Observer { result ->
            if (result){
                findNavController().navigate(R.id.mapView)
            }
        })

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
                    loginViewModel.login(loginResult.accessToken.token).observe(this@LoginView, Observer {response ->
                        if (response.status == Status.SUCCESS){
                            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                            if (sharedPref != null) {
                                with (sharedPref.edit()){
                                    putString(getString(R.string.preferences_userSystemId), response.data!!.user.id.toString())
                                    putString(getString(R.string.preferences_token), response.data.token)
                                    commit()
                                }
                            }
                            loginViewModel.addUserToDatabase(response.data!!.user.asUserBrief())
                            findNavController().navigate(R.id.mapView)
                        }
                        else if (response.status == Status.UNAUTHORIZED){
                            loginViewModel.registerUserFromFacebook(loginResult.accessToken.token).observe(this@LoginView, Observer {
                                loginViewModel.getUserBySystemId(it).observe(this@LoginView, Observer { user ->
                                    loginViewModel.addUserToDatabase(user!!.asUserBrief())
                                })
                                findNavController().navigate(R.id.mapView)
                            })
                        }
                        else{
                            val snack = Snackbar.make(requireView(), response.message!!, Snackbar.LENGTH_LONG)
                            snack.show()
                        }
                    })
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                    val snack = Snackbar.make(requireView(), "Wystąpił błąd", Snackbar.LENGTH_LONG)
                    snack.show()
                }
            })
        }
        catch (ex: Exception){
            Toast.makeText(requireContext(), ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initLoginViewModel() {
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(requireContext())).get(LoginViewModel::class.java)
    }
}
